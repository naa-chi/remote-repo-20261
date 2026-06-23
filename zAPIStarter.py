import threading, time, webbrowser, sys
import zCreateAllDatabases
import zMenu

url = "http://localhost:7769"   # api-gateway port. read readme.txt 
projecturls = [7770, 7771, 7772, 7773, 7774, 7775, 7776, 7777, 7778, 7779, 7780, 7781, 7782]
delayTime = 15*14+30                      
# given it takes ~15 seconds to initialize a microservice, and we have 14 of the damn things, this way makes the most sense
# 210 seconds of init time is a bit crazy though. +30 for 

def open_browser():
    print(f"Waiting {delayTime} to launch")
    time.sleep(delayTime)
    webbrowser.open(url)  # open api-gateway (port 7769)
    for port in projecturls:
        time.sleep(0.5)
        webbrowser.open(f"http://localhost:{port}/doc/swagger-ui.html") # changed to automatically send to swagger docs
    print(f"Browser opened at {url}")

if __name__ == "__main__":
    print("Launching all microservices.") #Does include api-gateway in here
    starter_thread = threading.Thread(target=zCreateAllDatabases.main, daemon=True)
    starter_thread.start()
    # Opens the browser and keeps thread alive
    open_browser()
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print("\nEarly shutdown. Use the stop script to cleanly terminate.")
        zMenu.main()
    #i guess bro