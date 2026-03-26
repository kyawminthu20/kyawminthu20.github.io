3. Asynchronous Faults in Distributed Systems
   What they’re testing
   Real-world complexity
   Multi-device coordination (PLC, drives, sensors, IPC)
   The problem

Faults don’t happen cleanly:

Network delays
Race conditions
Partial failures
Strong answer structure
Step 1 — Detection
Heartbeats (PLC ↔ devices)
Watchdogs
Timeout monitoring
Step 2 — Classification
Critical → stop immediately
Non-critical → degrade or warn
Step 3 — Response
Transition system to FAULT state
Log fault with timestamp + source
Step 4 — Recovery
Manual reset vs auto recovery logic
Example answer

“I design fault handling to be event-driven. Each subsystem reports status independently, and the main controller aggregates health. If communication drops or a subsystem fails, I trigger a controlled transition to a safe state rather than letting inconsistent states propagate.”

What NOT to say
“I just check bits and alarms”
“If something fails, I stop everything” (too simplistic)
