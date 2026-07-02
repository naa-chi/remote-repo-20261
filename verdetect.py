import os
import re

def parse_application_properties(dirpath):
    """
    Scans the directory and parent directories up to the module root 
    to find and parse application.properties or application.yml.
    """
    service_name = None
    port = "8080"  # Spring Boot default port

    # Helper regex patterns
    prop_name_re = re.compile(r'spring\.application\.name\s*=\s*(.+)')
    prop_port_re = re.compile(r'server\.port\s*=\s*(.+)')
    yml_name_re = re.compile(r'name:\s*["\']?([^"\']+)["\']?')
    yml_port_re = re.compile(r'port:\s*(\d+)')

    # Traverse upwards or search current module directory
    current = dirpath
    while current and current != os.path.dirname(current):
        config_files = []
        for root, _, files in os.walk(current):
            for f in files:
                if f in ["application.properties", "application.yml", "application.yaml"]:
                    config_files.append(os.path.join(root, f))
        
        if config_files:
            for config_path in config_files:
                try:
                    with open(config_path, 'r', encoding='utf-8') as f:
                        content = f.read()
                        if config_path.endswith('.properties'):
                            for line in content.splitlines():
                                line = line.strip()
                                n_match = prop_name_re.match(line)
                                p_match = prop_port_re.match(line)
                                if n_match: service_name = n_match.group(1).strip()
                                if p_match: port = p_match.group(1).strip()
                        else:  # YAML processing
                            # Simple line-by-line fallback for nested YAML keys
                            for line in content.splitlines():
                                if "name:" in line:
                                    m = yml_name_re.search(line)
                                    if m: service_name = m.group(1).strip()
                                if "port:" in line:
                                    m = yml_port_re.search(line)
                                    if m: port = m.group(1).strip()
                except Exception:
                    pass
            if service_name or port != "8080":
                break
        current = os.path.dirname(current)

    return service_name, port

def extract_api_endpoints(root_dir):
    """Scans Spring Boot Controller files, mapping service configuration and API routes."""
    print("=================================================================")
    print("                 API ENDPOINT EXTRACTION MATRIX                  ")
    print("=================================================================\n")

    class_mapping_pattern = re.compile(r'@RequestMapping\s*\(\s*(?:value\s*=\s*)?["\']([^"\']+)["\']\s*\)')
    method_mapping_pattern = re.compile(r'@(Get|Post|Put|Delete|Patch)Mapping\s*(?:\(\s*(?:value\s*=\s*|path\s*=\s*)?["\']([^"\']*)["\']\s*\))?')

    total_endpoints = 0
    results = {}

    for dirpath, _, filenames in os.walk(root_dir):
        for filename in filenames:
            if filename.endswith("Controller.java"):
                filepath = os.path.join(dirpath, filename)
                
                # Resolve service meta-data from configuration files
                detected_name, port = parse_application_properties(dirpath)
                
                if not detected_name:
                    parts = os.path.normpath(filepath).split(os.sep)
                    # Fallback to the root directory name of the module
                    detected_name = parts[1] if len(parts) > 1 else "UnknownService"

                service_key = f"{detected_name} (Port: {port})"
                
                if service_key not in results:
                    results[service_key] = []

                try:
                    with open(filepath, 'r', encoding='utf-8') as file:
                        content = file.readlines()

                    class_base_path = ""
                    
                    for line in content:
                        line = line.strip()
                        
                        class_match = class_mapping_pattern.search(line)
                        if class_match:
                            class_base_path = class_match.group(1)
                            if not class_base_path.startswith("/"):
                                class_base_path = "/" + class_base_path
                            if class_base_path.endswith("/"):
                                class_base_path = class_base_path[:-1]
                            continue

                        method_match = method_mapping_pattern.search(line)
                        if method_match:
                            http_method = method_match.group(1).upper()
                            method_path = method_match.group(2) if method_match.group(2) else ""
                            
                            if method_path and not method_path.startswith("/"):
                                method_path = "/" + method_path
                            
                            full_path = f"{class_base_path}{method_path}"
                            if full_path == "":
                                full_path = "/"
                                
                            results[service_key].append((http_method, full_path, filename))
                            total_endpoints += 1

                except Exception as e:
                    print(f"[ERROR] Failed to read {filepath}: {str(e)}")

    # Print formatted matrix
    for service_info, endpoints in sorted(results.items()):
        if endpoints:
            print(f"SERVICE: {service_info.upper()}")
            print("-" * 90)
            print(f"{'METHOD':<10} | {'ENDPOINT PATH':<45} | {'CONTROLLER'}")
            print("-" * 90)
            for method, path, controller in sorted(endpoints, key=lambda x: x[1]):
                print(f"[{method:<8}] | {path:<45} | {controller}")
            print("\n")

    print("=================================================================")
    print(f"Extraction complete. Total distinct endpoints identified: {total_endpoints}")
    print("=================================================================")

if __name__ == "__main__":
    current_directory = os.getcwd()
    extract_api_endpoints(current_directory)