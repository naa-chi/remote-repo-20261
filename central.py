
# Central list of all microservices (except api-gateway, which has no DB).
# Add new services here in the desired startup order.

STARTUP_ORDER = [
    "auth",          # port 7780
    "city",          # port 7777
    "client",        # port 7779
    "driver",        # port 7778
    "line",          # port 7774
    "maintenance",   # port 7775
    "manufacturer",  # port 7770
    "review",        # port 7782
    "station",       # port 7773
    "ticket",        # port 7776
    "train",         # port 7771
    "typeengine",    # port 7772
    "supervisor",    # port 7781
    # ---------- ADD NEW SERVICES HERE ----------
    # "newservice",  # uncomment and assign next free port (e.g., 7783)
]