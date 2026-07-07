import os
import json

# Define the root directory of your Spring Boot project
# Change "." to the specific path if running the script from outside the project root
PROJECT_ROOT = "."
OUTPUT_FILE = "project_context.json"

# File extensions and specific filenames to include as relevant context
TARGET_EXTENSIONS = {".java", ".properties", ".yml", ".yaml", ".sql", ".xml", ".gradle"}
TARGET_FILES = {"pom.xml", "build.gradle", "Dockerfile", "docker-compose.yml"}

def should_include_file(file_name):
    if file_name in TARGET_FILES:
        return True
    _, ext = os.path.splitext(file_name)
    return ext.lower() in TARGET_EXTENSIONS

def generate_project_context(root_dir):
    project_context = {
        "project_root": os.path.abspath(root_dir),
        "structure": [],
        "files": {}
    }

    for dirpath, dirnames, filenames in os.walk(root_dir):
        # Exclude common build and IDE directories to keep context clean
        dirnames[:] = [d for d in dirnames if d not in {".git", ".idea", "target", "build", ".gradle", "bin"}]
        
        # Record directory structure relative to root
        rel_dir = os.path.relpath(dirpath, root_dir)
        if rel_dir != ".":
            project_context["structure"].append(rel_dir)

        for filename in filenames:
            if should_include_file(filename):
                full_path = os.path.join(dirpath, filename)
                rel_path = os.path.relpath(full_path, root_dir)
                
                try:
                    with open(full_path, 'r', encoding='utf-8', errors='replace') as f:
                        content = f.read()
                    
                    project_context["files"][rel_path] = {
                        "filename": filename,
                        "path": rel_path,
                        "content": content
                    }
                except Exception as e:
                    project_context["files"][rel_path] = {
                        "filename": filename,
                        "path": rel_path,
                        "error": f"Could not read file: {str(e)}"
                    }

    return project_context

if __name__ == "__main__":
    print(f"Mapping project from root: {os.path.abspath(PROJECT_ROOT)}...")
    context_data = generate_project_context(PROJECT_ROOT)
    
    with open(OUTPUT_FILE, 'w', encoding='utf-8') as json_file:
        json.dump(context_data, json_file, indent=4, ensure_ascii=False)
        
    print(f"Project context successfully saved to {OUTPUT_FILE}")