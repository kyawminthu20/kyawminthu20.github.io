2. Interlocks vs Permissives vs Safety Trips

This is a core industry filter question.

Clean definitions
Permissives (before start)

Conditions required to START:

Air pressure OK
Guards closed
Temperature within range

👉 If not met → machine won’t start

Interlocks (during operation)

Conditions required to KEEP RUNNING:

Flow maintained
Motor feedback OK
Process stable

👉 If broken → controlled stop

Safety Trips (hazard response)

Hard safety events:

E-stop
Safety gate opened
Overpressure (SIL/PL logic)

👉 Immediate safe shutdown (de-energize to trip)

Strong answer

“Permissives allow start, interlocks maintain operation, and safety trips override everything to bring the system to a safe state. I separate these in logic layers and treat safety independently from standard control.”

What they’re testing deeper
Do you understand functional safety separation
Do you mix safety with PLC logic (bad sign)
Can you design layered control logic
Do you understand the implications of safety trip events
