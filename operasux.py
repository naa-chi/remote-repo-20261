import requests
import json

GATEWAY_URL = "http://localhost:7769"

# Collected array of unique GET endpoints mapped strictly through the gateway
endpoints = [
    "/",
    "/api/auth",
    "/api/auth/validate/test-code",
    "/api/auth/1",
    "/api/cities",
    "/api/cities/code/test-code",
    "/api/clients",
    "/api/clients/validate/test-code",
    "/api/clients/1",
    "/api/drivers",
    "/api/drivers/1",
    "/api/maintenances",
    "/api/maintenances/vehicle/1",
    "/api/maintenances/1",
    "/api/manufacturers",
    "/api/manufacturers/country/USA",
    "/api/manufacturers/name/test-name",
    "/api/manufacturers/1",
    "/api/reviews",
    "/api/reviews/client/1",
    "/api/reviews/rating/5",
    "/api/reviews/train/1",
    "/api/reviews/1",
    "/api/supervisors",
    "/api/supervisors/validate/test-code",
    "/api/supervisors/1",
    "/api/tickets",
    "/api/tickets/code/test-code",
    "/api/tickets/destination/NYC",
    "/api/tickets/origin/CHI",
    "/api/tickets/1",
    "/api/trains",
    "/api/trains/id/1",
    "/api/trains/manufacturer/1",
    "/api/trains/type/1",
    "/api/trains/test-code",
    "/api/trainstype",
    "/api/trainstype/1",
    "/api/engines",
    "/api/engines/1"
]

def ping_gateway_endpoints():
    print(f"Starting API Gateway connectivity sweep on: {GATEWAY_URL}\n")
    
    for path in endpoints:
        url = f"{GATEWAY_URL}{path}"
        print("-" * 80)
        print(f"➡️ GET {url}")
        
        try:
            response = requests.get(url, timeout=5)
            print(f"Status Code: {response.status_code}")
            
            # Try parsing response content to JSON, fallback to raw text if it's not structured
            try:
                data = response.json()
                print("Response JSON:")
                print(json.dumps(data, indent=2))
            except json.JSONDecodeError:
                if response.text.strip():
                    print(f"Response Text (Non-JSON): {response.text.strip()[:200]}")
                else:
                    print("Response Body: Empty")
                    
        except requests.exceptions.RequestException as e:
            print(f"❌ Connection failed: {e}")
            
    print("-" * 80)
    print("\nConnectivity sweep complete.")

if __name__ == "__main__":
    ping_gateway_endpoints()