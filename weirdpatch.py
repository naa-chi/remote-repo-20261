import os
import xml.etree.ElementTree as ET

def auto_fix_poms():
    root_dir = os.path.dirname(os.path.abspath(__file__))
    print("=================================================================")
    print("              AUTOMATED POM.XML REPAIR UTILITY                   ")
    print("=================================================================\n")
    
    namespaces = {'maven': 'http://maven.apache.org/POM/4.0.0'}
    # Ensure XML output preserves the clean, default Maven namespace formatting
    ET.register_namespace('', 'http://maven.apache.org/POM/4.0.0')
    
    fixed_count = 0

    for dirpath, _, filenames in os.walk(root_dir):
        if 'pom.xml' in filenames:
            pom_path = os.path.join(dirpath, 'pom.xml')
            folder_name = os.path.basename(dirpath)
            
            # Skip the root aggregator pom if one exists
            if dirpath == root_dir:
                continue

            try:
                tree = ET.parse(pom_path)
                root = tree.getroot()
                
                # Target the parent-level artifactId and name configuration strings
                artifact_id_elem = root.find('maven:artifactId', namespaces)
                name_elem = root.find('maven:name', namespaces)
                
                needs_update = False
                
                if artifact_id_elem is not None and artifact_id_elem.text.strip() != folder_name:
                    print(f"🔧 Updating <artifactId> in {folder_name}/pom.xml: '{artifact_id_elem.text.strip()}' -> '{folder_name}'")
                    artifact_id_elem.text = folder_name
                    needs_update = True
                    
                if name_elem is not None and name_elem.text.strip() != folder_name:
                    print(f"🔧 Updating <name>       in {folder_name}/pom.xml: '{name_elem.text.strip()}' -> '{folder_name}'")
                    name_elem.text = folder_name
                    needs_update = True
                
                if needs_update:
                    # Overwrite the target pom.xml file with corrected configurations
                    tree.write(pom_path, encoding='UTF-8', xml_declaration=True)
                    print(f"  ↳ [FIXED] '{folder_name}' project file rewritten successfully.\n")
                    fixed_count += 1
                    
            except Exception as e:
                print(f"[❌ ERROR] Could not modify file in folder '{folder_name}': {e}")

    print("=================================================================")
    print(f"Execution complete. Total pom.xml files repaired: {fixed_count}")
    print("You can safely run 'RUN ME RUN ME.py' now.")
    print("=================================================================")

if __name__ == "__main__":
    auto_fix_poms()