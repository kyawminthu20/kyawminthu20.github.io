# Control System Machine State Model

1. Machine State Model
   What they’re testing
   Can you design structured control logic, not just write ladder
   Do you think in systems, not rungs
   Core concept

A machine should always be in a clearly defined state.

Typical state model
INIT → IDLE → READY → RUN → PAUSE → FAULT → RECOVERY
Strong answer (how to explain)

“I use a finite state machine approach. Each state has defined entry conditions, actions, and exit conditions. Transitions are event-driven, not scattered across logic.”

Then add depth:

Separate state logic from IO logic
Use central state manager
Fault overrides all states → forces FAULT state
Example (simple)
READY → RUN only if:
all permissives = true
no active faults
Any fault → immediate transition to FAULT

You’re going to get asked this in interviews because it reveals whether you think like a system designer or just a PLC programmer.

1. What is a Control System State Model?

A state model defines:

What states a machine can be in
What conditions allow transitions
What actions happen in each state

In simple terms:

A structured way to control what the machine is doing now and what it is allowed to do next

2. The Core Idea (Don’t Overcomplicate It)

Every real machine behaves like this:

STOPPED → STARTING → RUNNING → STOPPING → STOPPED
↓
FAULT

A state model enforces order and safety:

No jumping randomly between actions
No unsafe transitions
Deterministic behavior 3. Main Types of State Models (What You Should Know)

There isn’t “one official number,” but in controls engineering, these are the 5 that matter:

1. Finite State Machine (FSM) — Most important

Definition:

System has a finite number of states
Transitions triggered by events/conditions

Example:

IDLE → RUN → FAULT

Used in:

PLC logic (very common)
Robotics sequences
Embedded systems

Key traits:

Deterministic
Easy to debug
Clear transitions

Weakness:

Can become messy for complex systems 2. Hierarchical State Machine (HSM)

Definition:

States can contain sub-states

Example:

RUNNING
├── AUTO
└── MANUAL

Used in:

Complex machines
Robotics systems
Multi-mode equipment

Why it matters:

Reduces complexity vs flat FSM
Matches real machine structure 3. Statecharts (Extended FSM)

Definition:

FSM + hierarchy + parallel states + events

Example:

Machine running AND safety monitoring simultaneously

Used in:

Advanced automation
Model-based design (MATLAB/Simulink)
High-end robotics

Key advantage:

Handles concurrency + complexity 4. Sequential / Step-Based Model (SFC / GRAFCET)

Definition:

Process flows through steps with transitions

Example:

Step 1 → Step 2 → Step 3

Used in:

PLC programming (IEC 61131-3 SFC)
Batch processes
Assembly lines

Key difference vs FSM:

Focus is sequence progression, not system mode 5. Continuous State Model (Mathematical Model)

Definition:

System described by equations, not discrete states

Examples:

PID control
State-space models

Form:

ẋ = Ax + Bu
y = Cx + Du

Used in:

Control theory
Servo systems
Advanced motion control

Key difference:

Continuous dynamics vs discrete logic 4. Practical Comparison (This is what interviewers care about)
Model Type Discrete / Continuous Best Use Case Complexity
FSM Discrete Machine modes Low–Medium
HSM Discrete Multi-mode machines Medium
Statecharts Discrete + concurrent Complex systems High
SFC / Sequential Discrete Step-by-step processes Medium
Continuous Model Continuous Motion / PID / physics High 5. The Real Engineering Insight (What Most Candidates Miss)

A real system uses multiple models together:

FSM → machine modes (RUN / STOP / FAULT)
SFC → process sequence
Continuous model → motor control (PID loop)

Good engineers separate these layers. Weak ones mix everything into ladder logic.

6. How to Answer in an Interview (Concise Version)

If they ask:

“How do you structure a machine state model?”

You say:

Top level: FSM (Idle, Run, Fault, Maintenance)
Inside Run: SFC or sequence logic
Continuous loops handled separately (PID, motion control)
Fault handling is asynchronous and can interrupt any state

That answer signals:

You understand architecture
You separate concerns
You design for reliability 7. Common Mistakes (Avoid These)
Mixing sequence logic with state logic
No clear fault state
No transition conditions defined
Allowing illegal transitions
Embedding everything in ladder without structure
Bottom Line

There isn’t a fixed number of “state models,” but in practice:

You need to understand 5 core models and how they work together.

If you want, I can map this directly to:

Studio 5000 implementation (ladder + AOI structure)
A real Amazon conveyor or robotics example
Or turn this into a whiteboard answer for your Velo3D interview

That’s where this becomes high-value.

This is where most candidates break down. They can _name_ FSM or SFC, but they can’t show how to **engineer it end-to-end**.

I’ll walk you through it the way a controls lead would expect:
**Design → Implementation → Testing → Commissioning**

---

# 1. DESIGN — Architecture First (Not Ladder Yet)

## 1.1 Define the Machine Contract

Start with **clear states and intent**, not code.

### Core states (standard pattern)

```text
OFF
IDLE
STARTING
RUNNING
STOPPING
FAULT
MAINTENANCE (optional)
```

### Define for each state:

- Entry conditions
- Exit conditions
- Allowed transitions
- Outputs/behavior

Example (RUNNING):

| Item      | Definition                           |
| --------- | ------------------------------------ |
| Entry     | Start command + all permissives true |
| Exit      | Stop command OR fault                |
| Actions   | Motors ON, sequences active          |
| Forbidden | Direct jump to OFF                   |

---

## 1.2 Separate Concerns (Critical)

You should **never mix these layers**:

| Layer              | Purpose                |
| ------------------ | ---------------------- |
| State Model (FSM)  | Machine mode           |
| Sequence (SFC)     | Step-by-step process   |
| Continuous Control | PID / motion           |
| Safety Layer       | Hardwired / safety PLC |
| Fault Manager      | Detection + handling   |

> This separation is what differentiates a system engineer from a ladder programmer.

---

## 1.3 Define Transitions (Deterministic)

Every transition must be explicit:

```text
IDLE → STARTING  : Start_PB AND Permissives_OK
STARTING → RUNNING : Speed_At_Setpoint
RUNNING → FAULT : Any_Fault
RUNNING → STOPPING : Stop_PB
```

No ambiguity. No hidden logic.

---

## 1.4 Fault Philosophy (You will get asked this)

Define:

- **Permissives** → allow start
- **Interlocks** → prevent damage
- **Trips (Faults)** → force stop

Example:

| Type       | Behavior             |
| ---------- | -------------------- |
| Permissive | Checked before start |
| Interlock  | Blocks action        |
| Trip       | Immediate shutdown   |

---

## 1.5 Mode Design (HSM concept)

```text
RUNNING
 ├── AUTO
 ├── MANUAL
 └── JOG
```

Each mode has:

- Different control authority
- Different safety rules

---

# 2. IMPLEMENTATION — PLC / Software Structure

## 2.1 Core Pattern (Do This in PLC)

Use an **enumerated state variable**:

```text
State = {OFF=0, IDLE=1, STARTING=2, RUNNING=3, STOPPING=4, FAULT=5}
```

---

## 2.2 Structured Logic Pattern

### Step 1 — State Register

- One variable controls entire system

### Step 2 — Transition Logic (Rising edge safe)

```ladder
IF State == IDLE AND Start_PB AND Permissives_OK THEN
    State := STARTING
END_IF
```

---

### Step 3 — State Actions (Separated)

```ladder
IF State == RUNNING THEN
    Conveyor_Run := TRUE
    Enable_Sequence := TRUE
END_IF
```

---

### Step 4 — Fault Override (Global, highest priority)

```ladder
IF Any_Fault THEN
    State := FAULT
END_IF
```

> Fault logic must **override everything**, always.

---

## 2.3 SFC (Sequence Layer)

Inside RUNNING:

```text
Step 1: Start conveyor
Step 2: Wait sensor
Step 3: Activate actuator
Step 4: Confirm completion
```

This should **NOT control machine mode**.

---

## 2.4 Continuous Control (Separate Task)

- PID loop in periodic task
- Motion control in drive / controller

Never embed inside FSM.

---

## 2.5 Recommended PLC Structure (Studio 5000)

```text
MainRoutine
 ├── StateManager
 ├── FaultManager
 ├── ModeManager
 ├── SequenceControl (SFC or logic)
 ├── Motion/PID
```

---

# 3. TESTING — Before You Touch Hardware

## 3.1 Simulation First (Non-negotiable)

Test:

- All transitions
- Fault conditions
- Illegal states

### Checklist:

- Can it start?
- Can it stop cleanly?
- Does fault interrupt immediately?
- Can it recover?

---

## 3.2 Transition Coverage Matrix

| From    | To       | Condition      | Tested |
| ------- | -------- | -------------- | ------ |
| IDLE    | STARTING | Start PB       | ✔      |
| RUNNING | FAULT    | Motor overload | ✔      |
| FAULT   | IDLE     | Reset          | ✔      |

If you don’t have this → you’re guessing.

---

## 3.3 Fault Injection Testing

Simulate:

- Sensor failure
- Drive fault
- Network loss

Expected behavior:

- Deterministic response
- Safe state reached

---

## 3.4 Timing / Race Conditions

Test:

- Simultaneous events
- Rapid start/stop
- Communication delays

This is where weak systems fail.

---

# 4. COMMISSIONING — Real Machine

## 4.1 Dry Run (No Power to Actuators)

- Verify state transitions
- Verify logic flow
- No motion yet

---

## 4.2 I/O Checkout

- Sensors mapped correctly
- Actuators respond correctly
- No inverted signals

---

## 4.3 Controlled Startup

Sequence:

1. Manual mode
2. Jog testing
3. Single-step sequence
4. Full automatic

---

## 4.4 Fault Validation (Critical)

Force:

- E-stop
- Overload
- Sensor failure

Verify:

- Immediate safe stop
- Correct state = FAULT
- No unintended motion

---

## 4.5 Recovery Testing

- Reset behavior
- Restart from safe state
- No unexpected transitions

---

# 5. WHAT INTERVIEWERS ARE REALLY LOOKING FOR

They are testing if you understand:

### 1. Determinism

- Same input → same outcome

### 2. Safety-first design

- Fault overrides everything

### 3. Separation of concerns

- State ≠ Sequence ≠ PID

### 4. Debuggability

- Can a technician understand it at 2 AM?

---

# 6. Real-World Example (Amazon Conveyor Mindset)

### FSM:

- IDLE → RUN → FAULT

### Sequence:

- Zone release → sensor detect → transfer

### Continuous:

- VFD speed control

### Fault:

- Jam → immediate stop upstream/downstream

---

# 7. Common Failure Patterns (You should avoid)

- One giant ladder file (no structure)
- No clear state variable
- Fault logic buried inside sequence
- No test coverage
- No recovery strategy

---

# Bottom Line

A proper control system state model is:

> **Architected, not coded**

If you want to stand out in that Velo3D interview, say this clearly:

- “I design state models first, define transitions explicitly, separate sequence and continuous control, and validate with simulation and fault injection before commissioning.”

If you want next step, I can:

- Turn this into **Studio 5000 code template (AOI + UDT)**
- Or simulate a **multi-axis machine example with faults and recovery**

That’s the level they’re hiring for.

Below is both:

1. a **Studio 5000-style code template** using **UDT + AOI pattern**
2. a **multi-axis machine example** with **faults, recovery, and commissioning logic**

I will keep it practical, not academic.

---

# Part 1 — Studio 5000 Code Template (UDT + AOI)

## 1. Architecture

For a real machine, split it like this:

```text
MainTask
 ├── SafetyStatus         // read safety chain / safe inputs
 ├── MachineStateAOI      // top-level machine FSM
 ├── Axis1_ControlAOI     // servo axis 1 control
 ├── Axis2_ControlAOI     // servo axis 2 control
 ├── SequenceManagerAOI   // process steps
 ├── FaultManagerAOI      // collect and prioritize faults
 └── HMI_Interface        // commands, status, alarms
```

## 2. Top-Level Design Intent

### Top-level machine states

```text
0 = OFF
10 = IDLE
20 = STARTING
30 = HOMING
40 = READY
50 = AUTO_RUN
60 = MANUAL
70 = STOPPING
80 = FAULT
90 = RECOVERY
```

### Why this matters

- **Machine state** controls machine mode
- **Axis AOIs** control local axis behavior
- **Sequence manager** handles process steps
- **Fault manager** can force machine to FAULT from anywhere

That separation is the whole point.

---

## 3. UDT Definitions

These are example UDTs you would create in Studio 5000.

---

## 3.1 `UDT_MachineCmd`

```text
StartPB               : BOOL
StopPB                : BOOL
ResetPB               : BOOL
AutoModeCmd           : BOOL
ManualModeCmd         : BOOL
HomeCmd               : BOOL
CycleStartCmd         : BOOL
CycleStopCmd          : BOOL
EStopOK               : BOOL
SafetyOK              : BOOL
```

---

## 3.2 `UDT_MachineStatus`

```text
State                 : DINT
PrevState             : DINT
StateChanged          : BOOL
AutoMode              : BOOL
ManualMode            : BOOL
ReadyToStart          : BOOL
Running               : BOOL
Faulted               : BOOL
FaultCode             : DINT
PermissivesOK         : BOOL
InterlocksOK          : BOOL
RecoveryRequired      : BOOL
```

---

## 3.3 `UDT_AxisCmd`

```text
Enable                : BOOL
Home                  : BOOL
JogFwd                : BOOL
JogRev                : BOOL
MoveAbs               : BOOL
MoveRel               : BOOL
Stop                   : BOOL
ResetFault            : BOOL
TargetPosition        : REAL
TargetVelocity        : REAL
TargetAccel           : REAL
```

---

## 3.4 `UDT_AxisStatus`

```text
Enabled               : BOOL
Homed                 : BOOL
InPosition            : BOOL
Moving                : BOOL
Standstill            : BOOL
Faulted               : BOOL
Warning               : BOOL
ServoReady            : BOOL
ActualPosition        : REAL
ActualVelocity        : REAL
FollowingError        : REAL
FaultCode             : DINT
HomeComplete          : BOOL
MoveDone              : BOOL
```

---

## 3.5 `UDT_FaultRecord`

```text
Active                : BOOL
Latched               : BOOL
Code                  : DINT
Severity              : DINT
RequiresReset         : BOOL
MessageID             : DINT
Timestamp_ms          : DINT
```

---

## 4. AOI Structure

## 4.1 AOI: `AOI_MachineStateManager`

### Inputs

- `Cmd : UDT_MachineCmd`
- `Axis1Sts : UDT_AxisStatus`
- `Axis2Sts : UDT_AxisStatus`
- `AnyFault : BOOL`
- `FaultCodeIn : DINT`

### InOut

- `Sts : UDT_MachineStatus`

### Outputs

- `AxisEnableCmd : BOOL`
- `HomeAllCmd : BOOL`
- `AutoSequenceEnable : BOOL`
- `ManualEnable : BOOL`

---

## 4.2 AOI Internal Rules

### Permissives example

```text
PermissivesOK =
    Cmd.EStopOK
    AND Cmd.SafetyOK
    AND NOT AnyFault
```

### Interlocks example

```text
InterlocksOK =
    Axis1Sts.ServoReady
    AND Axis2Sts.ServoReady
```

---

## 5. State Engine Logic

Studio 5000 usually does this with ladder or Structured Text inside routines.
I will show it in **Structured Text style**, because it is cleaner.

## 5.1 State transition core

```pascal
// Detect state change
Sts.StateChanged := (Sts.State <> Sts.PrevState);
IF Sts.StateChanged THEN
    Sts.PrevState := Sts.State;
END_IF;

// Defaults
AxisEnableCmd := 0;
HomeAllCmd := 0;
AutoSequenceEnable := 0;
ManualEnable := 0;

Sts.PermissivesOK := Cmd.EStopOK AND Cmd.SafetyOK AND NOT AnyFault;
Sts.InterlocksOK := Axis1Sts.ServoReady AND Axis2Sts.ServoReady;

// Global fault override
IF AnyFault THEN
    Sts.State := 80; // FAULT
    Sts.Faulted := 1;
    Sts.FaultCode := FaultCodeIn;
END_IF;

// Main state machine
CASE Sts.State OF

    0: // OFF
        Sts.Running := 0;
        IF Cmd.EStopOK AND Cmd.SafetyOK THEN
            Sts.State := 10; // IDLE
        END_IF;

    10: // IDLE
        Sts.Running := 0;
        Sts.AutoMode := 0;
        Sts.ManualMode := 0;

        IF Cmd.ManualModeCmd THEN
            Sts.State := 60; // MANUAL
        ELSIF Cmd.AutoModeCmd AND Sts.PermissivesOK THEN
            Sts.State := 20; // STARTING
        END_IF;

    20: // STARTING
        AxisEnableCmd := 1;

        IF NOT Sts.PermissivesOK THEN
            Sts.State := 10; // back to IDLE
        ELSIF Sts.InterlocksOK THEN
            Sts.State := 30; // HOMING
        END_IF;

    30: // HOMING
        AxisEnableCmd := 1;
        HomeAllCmd := 1;

        IF Axis1Sts.HomeComplete AND Axis2Sts.HomeComplete THEN
            Sts.State := 40; // READY
        END_IF;

    40: // READY
        AxisEnableCmd := 1;
        Sts.AutoMode := 1;

        IF Cmd.CycleStartCmd THEN
            Sts.State := 50; // AUTO_RUN
        ELSIF Cmd.StopPB THEN
            Sts.State := 70; // STOPPING
        END_IF;

    50: // AUTO_RUN
        AxisEnableCmd := 1;
        AutoSequenceEnable := 1;
        Sts.AutoMode := 1;
        Sts.Running := 1;

        IF Cmd.CycleStopCmd OR Cmd.StopPB THEN
            Sts.State := 70; // STOPPING
        END_IF;

    60: // MANUAL
        AxisEnableCmd := 1;
        ManualEnable := 1;
        Sts.ManualMode := 1;

        IF Cmd.StopPB THEN
            Sts.State := 70;
        ELSIF Cmd.AutoModeCmd AND Sts.PermissivesOK THEN
            Sts.State := 20;
        END_IF;

    70: // STOPPING
        Sts.Running := 0;

        IF Axis1Sts.Standstill AND Axis2Sts.Standstill THEN
            Sts.State := 10; // IDLE
        END_IF;

    80: // FAULT
        Sts.Running := 0;
        Sts.Faulted := 1;
        Sts.RecoveryRequired := 1;

        IF Cmd.ResetPB AND NOT AnyFault AND Cmd.EStopOK AND Cmd.SafetyOK THEN
            Sts.State := 90; // RECOVERY
        END_IF;

    90: // RECOVERY
        AxisEnableCmd := 1;

        IF Axis1Sts.ServoReady AND Axis2Sts.ServoReady THEN
            Sts.Faulted := 0;
            Sts.RecoveryRequired := 0;
            Sts.FaultCode := 0;
            Sts.State := 10; // IDLE
        END_IF;

ELSE
    Sts.State := 80; // fail-safe to FAULT
    Sts.FaultCode := 999;
END_CASE;
```

---

## 6. Axis AOI Template

You should not put all motion logic in the top machine AOI.
Each axis needs its own control AOI.

## 6.1 `AOI_AxisControl`

### Inputs

- `Cmd : UDT_AxisCmd`
- drive feedback bits
- actual position / velocity
- alarm inputs

### InOut

- `Sts : UDT_AxisStatus`

### Outputs

- servo enable
- home request
- move absolute request
- jog commands
- stop request

---

## 6.2 Axis local state model

Example:

```text
0 = DISABLED
10 = ENABLING
20 = READY
30 = HOMING
40 = JOGGING
50 = POSITIONING
60 = STOPPING
80 = FAULT
```

### Axis ST example

```pascal
IF DriveFault THEN
    Sts.Faulted := 1;
    Sts.FaultCode := 2001;
END_IF;

CASE AxisState OF

    0: // DISABLED
        IF Cmd.Enable THEN
            AxisState := 10;
        END_IF;

    10: // ENABLING
        ServoEnableOut := 1;
        IF DriveReady THEN
            AxisState := 20;
            Sts.Enabled := 1;
        END_IF;

    20: // READY
        IF Cmd.Home THEN
            AxisState := 30;
        ELSIF Cmd.JogFwd OR Cmd.JogRev THEN
            AxisState := 40;
        ELSIF Cmd.MoveAbs THEN
            AxisState := 50;
        ELSIF Cmd.Stop THEN
            AxisState := 60;
        END_IF;

    30: // HOMING
        ServoEnableOut := 1;
        HomeCmdOut := 1;
        IF HomeDone THEN
            Sts.Homed := 1;
            Sts.HomeComplete := 1;
            AxisState := 20;
        END_IF;

    40: // JOGGING
        ServoEnableOut := 1;
        JogFwdOut := Cmd.JogFwd;
        JogRevOut := Cmd.JogRev;
        IF NOT Cmd.JogFwd AND NOT Cmd.JogRev THEN
            AxisState := 60;
        END_IF;

    50: // POSITIONING
        ServoEnableOut := 1;
        MoveAbsOut := 1;
        PositionCmdOut := Cmd.TargetPosition;
        VelocityCmdOut := Cmd.TargetVelocity;

        IF InPosition THEN
            Sts.MoveDone := 1;
            AxisState := 20;
        END_IF;

    60: // STOPPING
        StopCmdOut := 1;
        IF Standstill THEN
            AxisState := 20;
        END_IF;

    80: // FAULT
        StopCmdOut := 1;
        ServoEnableOut := 0;
        IF Cmd.ResetFault AND NOT DriveFault THEN
            Sts.Faulted := 0;
            Sts.FaultCode := 0;
            AxisState := 0;
        END_IF;
END_CASE;
```

---

## 7. Fault Manager Pattern

Do not scatter fault logic everywhere without ownership.

## 7.1 Typical fault sources

- E-stop not healthy
- safety relay dropped
- servo drive fault
- axis following error high
- home timeout
- move timeout
- part sensor disagreement
- clamp not confirmed
- network comm loss

## 7.2 Example priority scheme

| Priority | Type          | Action                           |
| -------- | ------------- | -------------------------------- |
| 1        | Safety trip   | immediate stop, force FAULT      |
| 2        | Motion fault  | stop axes, force FAULT           |
| 3        | Process fault | stop sequence, maybe recoverable |
| 4        | Warning       | alarm only                       |

## 7.3 Fault latching logic

```pascal
IF EStopDropped THEN
    Fault_EStop.Active := 1;
    Fault_EStop.Latched := 1;
    Fault_EStop.Code := 1001;
END_IF;

IF Axis1FollowingError > 5.0 THEN
    Fault_Axis1Follow.Active := 1;
    Fault_Axis1Follow.Latched := 1;
    Fault_Axis1Follow.Code := 2101;
END_IF;

AnyFault := Fault_EStop.Latched
         OR Fault_Axis1Follow.Latched
         OR Fault_Axis2Drive.Latched
         OR Fault_HomeTimeout.Latched;
```

---

# Part 2 — Multi-Axis Machine Example with Faults and Recovery

Now the simulation concept.

Let’s use a simple but realistic machine:

## Example machine: 2-axis pick-and-place gantry

- **Axis X** = horizontal travel
- **Axis Z** = vertical up/down
- gripper = part clamp output
- sensors:
  - part present
  - grip confirmed
  - home switches
  - safe zone clear

## Process cycle

1. Home X and Z
2. Move X to pick position
3. Move Z down
4. Grip part
5. Move Z up
6. Move X to place position
7. Move Z down
8. Release part
9. Move Z up
10. Return X home

---

## 1. State Layers

## Top machine state

```text
IDLE
STARTING
HOMING
READY
AUTO_RUN
STOPPING
FAULT
RECOVERY
```

## Sequence steps inside AUTO_RUN

```text
100 = WAIT_PART
110 = X_TO_PICK
120 = Z_DOWN_PICK
130 = GRIP_PART
140 = Z_UP_PICK
150 = X_TO_PLACE
160 = Z_DOWN_PLACE
170 = RELEASE_PART
180 = Z_UP_PLACE
190 = RETURN_HOME
200 = CYCLE_COMPLETE
```

---

## 2. Sequence Logic Example

```pascal
CASE SeqStep OF

    100: // WAIT_PART
        IF PartPresent THEN
            SeqStep := 110;
        END_IF;

    110: // X_TO_PICK
        AxisXCmd.MoveAbs := 1;
        AxisXCmd.TargetPosition := 100.0;
        IF AxisXSts.InPosition THEN
            SeqStep := 120;
        END_IF;

    120: // Z_DOWN_PICK
        AxisZCmd.MoveAbs := 1;
        AxisZCmd.TargetPosition := -50.0;
        IF AxisZSts.InPosition THEN
            SeqStep := 130;
        END_IF;

    130: // GRIP_PART
        GripperClose := 1;
        IF GripConfirmed THEN
            SeqStep := 140;
        END_IF;

    140: // Z_UP_PICK
        AxisZCmd.MoveAbs := 1;
        AxisZCmd.TargetPosition := 0.0;
        IF AxisZSts.InPosition THEN
            SeqStep := 150;
        END_IF;

    150: // X_TO_PLACE
        AxisXCmd.MoveAbs := 1;
        AxisXCmd.TargetPosition := 400.0;
        IF AxisXSts.InPosition THEN
            SeqStep := 160;
        END_IF;

    160: // Z_DOWN_PLACE
        AxisZCmd.MoveAbs := 1;
        AxisZCmd.TargetPosition := -45.0;
        IF AxisZSts.InPosition THEN
            SeqStep := 170;
        END_IF;

    170: // RELEASE_PART
        GripperClose := 0;
        IF NOT GripConfirmed THEN
            SeqStep := 180;
        END_IF;

    180: // Z_UP_PLACE
        AxisZCmd.MoveAbs := 1;
        AxisZCmd.TargetPosition := 0.0;
        IF AxisZSts.InPosition THEN
            SeqStep := 190;
        END_IF;

    190: // RETURN_HOME
        AxisXCmd.MoveAbs := 1;
        AxisXCmd.TargetPosition := 0.0;
        IF AxisXSts.InPosition THEN
            SeqStep := 200;
        END_IF;

    200: // CYCLE_COMPLETE
        CycleComplete := 1;
        SeqStep := 100;
END_CASE;
```

---

## 3. Fault Scenarios

This is what makes it real.

## 3.1 Fault A — Axis X following error

### Cause

- axis vibration
- servo unstable
- collision
- bad tuning

### Detection

```pascal
IF ABS(AxisXSts.FollowingError) > 3.0 THEN
    Fault_AxisXFollow.Latched := 1;
    Fault_AxisXFollow.Code := 2101;
END_IF;
```

### Response

- stop both axes
- drop sequence enable
- machine state to FAULT

### Why both axes?

Because one moving axis in a coordinated machine can create secondary collision risk.

---

## 3.2 Fault B — Z did not reach pick position in time

### Detection with timer

```pascal
IF SeqStep = 120 THEN
    TON_ZPickTimeout(IN := NOT AxisZSts.InPosition, PT := 3000);
END_IF;

IF TON_ZPickTimeout.Q THEN
    Fault_ZPickTimeout.Latched := 1;
    Fault_ZPickTimeout.Code := 2205;
END_IF;
```

### Likely causes

- obstruction
- drive torque limit
- wrong position target
- sensor mapping issue

---

## 3.3 Fault C — Grip failed

### Detection

```pascal
IF SeqStep = 130 THEN
    TON_GripTimeout(IN := NOT GripConfirmed, PT := 1000);
END_IF;

IF TON_GripTimeout.Q THEN
    Fault_GripFailed.Latched := 1;
    Fault_GripFailed.Code := 2302;
END_IF;
```

### Response

- stop cycle
- retract Z if safe
- fault machine
- operator recovery required

---

## 3.4 Fault D — Safety gate opened during motion

### Detection

```pascal
IF NOT Cmd.SafetyOK THEN
    Fault_SafetyOpen.Latched := 1;
    Fault_SafetyOpen.Code := 1002;
END_IF;
```

### Response

- immediate safe stop
- machine state FAULT
- reset only after gate closed and safety restored

---

## 4. Recovery Strategy

This is where many systems are weak.

Recovery must be deliberate, not “reset everything and hope”.

## 4.1 Recovery philosophy

When faulted, answer these:

- Are axes safe to re-enable?
- Is a part still held?
- Is Z down in a dangerous position?
- Is homing required again?
- Can sequence resume, or must it restart?

## 4.2 Example recovery states

```text
RECOVERY
 ├── VERIFY_SAFE
 ├── CLEAR_OUTPUTS
 ├── REENABLE_AXES
 ├── REHOME_REQUIRED
 └── RETURN_TO_IDLE
```

## 4.3 Recovery logic example

```pascal
CASE RecoveryStep OF

    10: // VERIFY_SAFE
        IF Cmd.EStopOK AND Cmd.SafetyOK AND NOT AnyFault THEN
            RecoveryStep := 20;
        END_IF;

    20: // CLEAR_OUTPUTS
        GripperClose := 0;
        AxisXCmd.Stop := 1;
        AxisZCmd.Stop := 1;
        RecoveryStep := 30;

    30: // REENABLE_AXES
        AxisXCmd.Enable := 1;
        AxisZCmd.Enable := 1;
        IF AxisXSts.ServoReady AND AxisZSts.ServoReady THEN
            RecoveryStep := 40;
        END_IF;

    40: // REHOME_REQUIRED
        AxisXCmd.Home := 1;
        AxisZCmd.Home := 1;
        IF AxisXSts.HomeComplete AND AxisZSts.HomeComplete THEN
            RecoveryStep := 50;
        END_IF;

    50: // RETURN_TO_IDLE
        RecoveryComplete := 1;
END_CASE;
```

### Good rule

After serious motion faults, **re-home before allowing production**.

---

## 5. Testing Plan

This is how you prove the design works.

## 5.1 Design verification

Create a transition matrix.

| From     | To       | Trigger                | Expected             |
| -------- | -------- | ---------------------- | -------------------- |
| IDLE     | STARTING | Auto mode + start      | enable sequence path |
| STARTING | HOMING   | drives ready           | home request issued  |
| HOMING   | READY    | both axes homed        | cycle can start      |
| AUTO_RUN | FAULT    | any latched fault      | motion stopped       |
| FAULT    | RECOVERY | reset + healthy system | controlled recovery  |
| RECOVERY | IDLE     | rehome complete        | ready for next start |

---

## 5.2 Sequence test list

| Test                           | Expected result      |
| ------------------------------ | -------------------- |
| normal cycle                   | completes all steps  |
| no part present                | waits at WAIT_PART   |
| grip failure                   | times out and faults |
| X following error              | immediate fault      |
| safety gate opens while moving | safe stop and fault  |
| operator reset after fault     | recovery path only   |

---

## 5.3 Commissioning sequence

## Phase 1 — I/O checkout

- verify home switches
- verify part sensor
- verify grip confirm
- verify servo ready / fault mapping
- verify E-stop and safety chain

## Phase 2 — axis standalone

- jog each axis slowly
- confirm direction
- confirm soft limits
- confirm home works repeatedly

## Phase 3 — dry sequence

- run sequence with gripper disabled
- simulate sensors if needed
- verify step transitions

## Phase 4 — live process

- low speed first
- one part at a time
- then production rate

## Phase 5 — fault injection

- disconnect grip confirm
- force following error threshold
- open safety gate
- stop drive enable
- verify recovery procedure

---

# Part 3 — How This Maps to Studio 5000 Tags

A practical tag layout:

## Program tags

```text
MachineCmd          : UDT_MachineCmd
MachineSts          : UDT_MachineStatus

AxisXCmd            : UDT_AxisCmd
AxisXSts            : UDT_AxisStatus

AxisZCmd            : UDT_AxisCmd
AxisZSts            : UDT_AxisStatus

Fault_EStop         : UDT_FaultRecord
Fault_AxisXFollow   : UDT_FaultRecord
Fault_ZPickTimeout  : UDT_FaultRecord
Fault_GripFailed    : UDT_FaultRecord

AnyFault            : BOOL
HighestFaultCode    : DINT
SeqStep             : DINT
RecoveryStep        : DINT
CycleComplete       : BOOL
GripperClose        : BOOL
PartPresent         : BOOL
GripConfirmed       : BOOL
```

---

# Part 4 — What You Would Say in an Interview

Use something close to this:

> I structure the machine with a top-level finite state machine for mode control, separate axis-level state logic for each servo, and a step sequence for process execution. Fault handling is asynchronous and has priority over all other transitions. During testing, I validate every legal transition, inject motion and sensor faults, and verify recovery behavior before full automatic commissioning. For multi-axis systems, I usually require re-homing after critical motion faults to guarantee known position before restarting.

That answer is solid.

---

# Part 5 — Brutally Honest Engineering Notes

## Good pattern

- one owner for machine state
- one owner per axis
- separate sequence manager
- fault manager has priority
- recovery is explicit

## Bad pattern

- one giant ladder routine
- random OTL/OTU bits everywhere
- sequence bits acting like machine mode
- reset clears faults without root-cause logic
- motion restart without rehome

That bad pattern is how machines become unreliable.

---

# Part 6 — Next-Level Upgrade

If you want to make this production-grade, the next thing is to build:

## A. Studio 5000 deliverables

- UDT list
- AOI pin definitions
- tag naming standard
- routine breakdown
- alarm/fault code table

## B. Simulation deliverables

- state transition table
- fault injection matrix
- commissioning checklist
- HMI screen layout for states / faults / recovery

I can turn this next into a **full Studio 5000 project skeleton with tag names, routines, AOI interfaces, and alarm table** in a format you can paste into your standards site or RAG.

Below is a **paste-ready markdown skeleton** for a **Studio 5000 multi-axis machine project**. It is structured so you can drop it into your standards site or RAG as a reference pattern.

---

# Studio 5000 Project Skeleton — Multi-Axis Machine State Model

## Purpose

This template defines a **clean, production-style Studio 5000 project structure** for a machine with:

- top-level machine state model
- axis-level control
- sequence control
- fault/alarm handling
- manual / auto modes
- recovery after faults

It is intended as a **reference architecture**, not a literal import file.

---

# 1. Design Objectives

## Goals

- deterministic machine behavior
- clear state ownership
- fault-first architecture
- separated concerns
- readable for troubleshooting and commissioning
- scalable to additional axes or stations

## Architectural Rules

- **Machine state** owns the machine mode
- **Sequence manager** owns process steps
- **Axis AOIs** own local axis behavior
- **Fault manager** owns fault latching and prioritization
- **Safety system** is external or separate from standard control logic
- no uncontrolled set/reset bit sprawl
- no hidden transitions

---

# 2. Example Machine Scope

## Example machine

Two-axis pick-and-place machine:

- Axis X = horizontal transfer
- Axis Z = vertical lift
- gripper output
- part-present sensor
- grip-confirm sensor
- home sensors
- safety chain healthy signal
- servo ready/fault feedback

---

# 3. Project Folder / Program Organization

## Controller Organizer Structure

```text
Controller
├── Tasks
│   ├── MainTask (Periodic, 20 ms)
│   │   └── MainProgram
│   ├── MotionTask (Periodic, 10 ms)         // optional depending on architecture
│   │   └── MotionProgram
│   ├── AlarmTask (Periodic, 100 ms)         // optional
│   │   └── AlarmProgram
│   └── SafetyStatusTask (Periodic, 20 ms)   // optional status handling only
│       └── SafetyProgram
│
├── Data Types
│   ├── UDT_MachineCmd
│   ├── UDT_MachineSts
│   ├── UDT_AxisCmd
│   ├── UDT_AxisSts
│   ├── UDT_Fault
│   ├── UDT_Alarm
│   ├── UDT_Sequence
│   ├── UDT_ModeReq
│   └── UDT_HMI
│
├── Add-On Instructions
│   ├── AOI_MachineStateMgr
│   ├── AOI_AxisControl
│   ├── AOI_SequenceMgr
│   ├── AOI_FaultMgr
│   ├── AOI_AlarmMgr
│   └── AOI_ModeMgr                      // optional
│
├── Programs
│   ├── MainProgram
│   │   ├── R00_Init
│   │   ├── R01_InputMap
│   │   ├── R02_ModeManager
│   │   ├── R03_FaultManager
│   │   ├── R04_MachineState
│   │   ├── R05_Sequence
│   │   ├── R06_AxisCommandBuild
│   │   ├── R07_OutputMap
│   │   ├── R08_HMI
│   │   └── R09_Diagnostics
│   │
│   ├── MotionProgram
│   │   ├── R10_AxisX_Control
│   │   ├── R11_AxisZ_Control
│   │   └── R12_MotionDiag
│   │
│   ├── AlarmProgram
│   │   ├── R20_AlarmProcessing
│   │   └── R21_AlarmAckReset
│   │
│   └── SafetyProgram
│       ├── R30_SafetyStatusRead
│       └── R31_SafetyPermissiveStatus
│
└── Controller Tags
```

---

# 4. Naming Standard

## Tag naming rules

### Controller / program tags

- `MachineCmd`
- `MachineSts`
- `AxisXCmd`
- `AxisXSts`
- `AxisZCmd`
- `AxisZSts`
- `Faults`
- `Alarms`
- `Seq`
- `HMI`
- `IO`
- `Cfg`

### Booleans

- status bits start with nouns or adjectives:
  - `ServoReady`
  - `Faulted`
  - `InPosition`
  - `CycleActive`

- command bits start with verbs or command words:
  - `StartCmd`
  - `StopCmd`
  - `ResetCmd`
  - `EnableCmd`

### DINT / INT states

- `MachineState`
- `MachinePrevState`
- `SeqStep`
- `RecoveryStep`
- `FaultCode`
- `AlarmCode`

### Timer names

- `Tmr_ZDownTimeout`
- `Tmr_GripConfirm`
- `Tmr_ResetHold`

### Avoid

- `B3_1`
- `Bit_12`
- `Temp1`
- `Run2`
- `LatchA`

Those names destroy maintainability.

---

# 5. UDT Definitions

## 5.1 UDT_MachineCmd

```text
StartPB                 : BOOL
StopPB                  : BOOL
ResetPB                 : BOOL
AutoModePB              : BOOL
ManualModePB            : BOOL
HomePB                  : BOOL
CycleStartPB            : BOOL
CycleStopPB             : BOOL

HMI_StartCmd            : BOOL
HMI_StopCmd             : BOOL
HMI_ResetCmd            : BOOL
HMI_AutoModeCmd         : BOOL
HMI_ManualModeCmd       : BOOL
HMI_HomeCmd             : BOOL
HMI_CycleStartCmd       : BOOL
HMI_CycleStopCmd        : BOOL

EStopOK                 : BOOL
SafetyOK                : BOOL
AirOK                   : BOOL
PowerOK                 : BOOL
SystemHealthy           : BOOL
```

## 5.2 UDT_MachineSts

```text
State                   : DINT
PrevState               : DINT
StateChanged            : BOOL

AutoMode                : BOOL
ManualMode              : BOOL
MaintenanceMode         : BOOL

ReadyToStart            : BOOL
Running                 : BOOL
Stopping                : BOOL
Faulted                 : BOOL
RecoveryRequired        : BOOL
Homed                   : BOOL

PermissivesOK           : BOOL
InterlocksOK            : BOOL

FaultCode               : DINT
HighestAlarmCode        : DINT
StateTimer_ms           : DINT
```

## 5.3 UDT_AxisCmd

```text
Enable                  : BOOL
Disable                 : BOOL
Home                    : BOOL
JogFwd                  : BOOL
JogRev                  : BOOL
MoveAbs                 : BOOL
MoveRel                 : BOOL
Stop                    : BOOL
ResetFault              : BOOL

TargetPosition          : REAL
TargetVelocity          : REAL
TargetAccel             : REAL
TargetDecel             : REAL
JogVelocity             : REAL
```

## 5.4 UDT_AxisSts

```text
AxisState               : DINT
PrevAxisState           : DINT
StateChanged            : BOOL

Enabled                 : BOOL
ServoReady              : BOOL
Homed                   : BOOL
HomeComplete            : BOOL
Moving                  : BOOL
Standstill              : BOOL
InPosition              : BOOL
Jogging                 : BOOL
Faulted                 : BOOL
Warning                 : BOOL

ActualPosition          : REAL
ActualVelocity          : REAL
FollowingError          : REAL

FaultCode               : DINT
MoveDone                : BOOL
HomeSensor              : BOOL
PosLimit                : BOOL
NegLimit                : BOOL
```

## 5.5 UDT_Fault

```text
Active                  : BOOL
Latched                 : BOOL
Acked                   : BOOL
ResetReq                : BOOL
Code                    : DINT
Severity                : DINT
MessageID               : DINT
SourceID                : DINT
Timestamp_ms            : DINT
```

## 5.6 UDT_Alarm

```text
Active                  : BOOL
Latched                 : BOOL
Acked                   : BOOL
Code                    : DINT
Priority                : DINT
MessageID               : DINT
```

## 5.7 UDT_Sequence

```text
Step                    : DINT
PrevStep                : DINT
StepChanged             : BOOL
StepTimer_ms            : DINT
CycleActive             : BOOL
CycleComplete           : BOOL
CycleAborted            : BOOL
PartPresent             : BOOL
GripConfirmed           : BOOL
SafeToLowerZ            : BOOL
```

## 5.8 UDT_HMI

```text
CurrentStateTextID      : DINT
CurrentStepTextID       : DINT
FaultBannerCode         : DINT
AlarmBannerCode         : DINT
OperatorMessageID       : DINT
```

---

# 6. Enumerated State Definitions

Use constants or documented DINT values.

## 6.1 Machine states

```text
0   OFF
10  IDLE
20  STARTING
30  HOMING
40  READY
50  AUTO_RUN
60  MANUAL
70  STOPPING
80  FAULT
90  RECOVERY
```

## 6.2 Axis states

```text
0   DISABLED
10  ENABLING
20  READY
30  HOMING
40  JOGGING
50  POSITIONING
60  STOPPING
80  FAULT
```

## 6.3 Sequence steps

```text
100 WAIT_PART
110 X_TO_PICK
120 Z_DOWN_PICK
130 GRIP_PART
140 Z_UP_PICK
150 X_TO_PLACE
160 Z_DOWN_PLACE
170 RELEASE_PART
180 Z_UP_PLACE
190 RETURN_HOME
200 CYCLE_COMPLETE
```

## 6.4 Recovery steps

```text
10  VERIFY_SAFE
20  CLEAR_OUTPUTS
30  REENABLE_AXES
40  REHOME_AXES
50  RETURN_TO_IDLE
```

---

# 7. Controller Tags

## 7.1 Core tags

```text
MachineCmd              : UDT_MachineCmd
MachineSts              : UDT_MachineSts

AxisXCmd                : UDT_AxisCmd
AxisXSts                : UDT_AxisSts

AxisZCmd                : UDT_AxisCmd
AxisZSts                : UDT_AxisSts

Seq                     : UDT_Sequence
HMI                     : UDT_HMI
```

## 7.2 Fault tags

```text
Fault_EStopDropped      : UDT_Fault
Fault_SafetyChainOpen   : UDT_Fault
Fault_AirLow            : UDT_Fault
Fault_PowerBad          : UDT_Fault

Fault_AxisXDrive        : UDT_Fault
Fault_AxisZDrive        : UDT_Fault
Fault_AxisXFollow       : UDT_Fault
Fault_AxisZFollow       : UDT_Fault

Fault_HomeTimeout       : UDT_Fault
Fault_ZDownTimeout      : UDT_Fault
Fault_GripTimeout       : UDT_Fault
Fault_PartMissing       : UDT_Fault

AnyFault                : BOOL
HighestFaultCode        : DINT
FaultResetAllowed       : BOOL
```

## 7.3 Alarm tags

```text
Alarm_PartLow           : UDT_Alarm
Alarm_CycleSlow         : UDT_Alarm
Alarm_MaintenanceDue    : UDT_Alarm
Alarm_AxisXWarning      : UDT_Alarm
Alarm_AxisZWarning      : UDT_Alarm

AnyAlarm                : BOOL
HighestAlarmCode        : DINT
```

## 7.4 Physical I/O abstraction tags

```text
IO_In_EStopHealthy              : BOOL
IO_In_SafetyRelayHealthy        : BOOL
IO_In_AirPressureOK             : BOOL
IO_In_PartPresent               : BOOL
IO_In_GripConfirmed             : BOOL
IO_In_X_Home                    : BOOL
IO_In_Z_Home                    : BOOL
IO_In_X_DriveReady              : BOOL
IO_In_Z_DriveReady              : BOOL
IO_In_X_DriveFault              : BOOL
IO_In_Z_DriveFault              : BOOL

IO_Out_GripperClose             : BOOL
IO_Out_TowerGreen               : BOOL
IO_Out_TowerAmber               : BOOL
IO_Out_TowerRed                 : BOOL
IO_Out_Buzzer                   : BOOL
```

## 7.5 Timers

```text
Tmr_StartingTimeout             : TON
Tmr_HomingTimeout               : TON
Tmr_ZDownTimeout                : TON
Tmr_GripConfirm                 : TON
Tmr_CycleSlow                   : TON
Tmr_ResetHold                   : TON
```

---

# 8. AOI Interfaces

## 8.1 AOI_MachineStateMgr

### Purpose

Owns top-level machine mode transitions.

### Inputs

```text
Cmd                     : UDT_MachineCmd
AxisXSts                : UDT_AxisSts
AxisZSts                : UDT_AxisSts
AnyFault                : BOOL
HighestFaultCode        : DINT
SeqCycleActive          : BOOL
SeqCycleComplete        : BOOL
```

### InOut

```text
Sts                     : UDT_MachineSts
```

### Outputs

```text
EnableAxes              : BOOL
HomeAllAxes             : BOOL
AllowAutoSequence       : BOOL
AllowManualControl      : BOOL
RequestRecovery         : BOOL
```

### Internal responsibilities

- evaluate permissives
- evaluate interlocks
- transition machine states
- enforce fault override
- issue top-level enable / home / sequence permissions

---

## 8.2 AOI_AxisControl

### Purpose

Owns one axis local state machine.

### Inputs

```text
Cmd                     : UDT_AxisCmd
DriveReady              : BOOL
DriveFault              : BOOL
HomeSensor              : BOOL
PosLimit                : BOOL
NegLimit                : BOOL
ActualPosition          : REAL
ActualVelocity          : REAL
FollowingError          : REAL
MoveDoneIn              : BOOL
HomeDoneIn              : BOOL
StandstillIn            : BOOL
InPositionIn            : BOOL
```

### InOut

```text
Sts                     : UDT_AxisSts
```

### Outputs

```text
ServoEnableOut          : BOOL
HomeCmdOut              : BOOL
JogFwdOut               : BOOL
JogRevOut               : BOOL
MoveAbsOut              : BOOL
MoveRelOut              : BOOL
StopCmdOut              : BOOL
PositionCmdOut          : REAL
VelocityCmdOut          : REAL
AccelCmdOut             : REAL
DecelCmdOut             : REAL
```

### Internal responsibilities

- axis state transitions
- drive ready / drive fault handling
- home control
- jog control
- point-to-point move control
- following error fault response
- stop and reset behavior

---

## 8.3 AOI_SequenceMgr

### Purpose

Owns process sequence only. Must not own machine state.

### Inputs

```text
MachineState            : DINT
AutoSequenceAllowed     : BOOL
PartPresent             : BOOL
GripConfirmed           : BOOL
AxisX_InPosition        : BOOL
AxisZ_InPosition        : BOOL
AxisX_ActualPosition    : REAL
AxisZ_ActualPosition    : REAL
AnyFault                : BOOL
CycleStartCmd           : BOOL
CycleStopCmd            : BOOL
```

### InOut

```text
Seq                     : UDT_Sequence
AxisXCmd                : UDT_AxisCmd
AxisZCmd                : UDT_AxisCmd
```

### Outputs

```text
GripperCloseCmd         : BOOL
CycleActive             : BOOL
CycleComplete           : BOOL
```

### Internal responsibilities

- process step transitions
- step timeout monitoring
- step-level output requests
- sequence complete / abort status

---

## 8.4 AOI_FaultMgr

### Purpose

Collects, latches, and prioritizes faults.

### Inputs

```text
EStopOK                 : BOOL
SafetyOK                : BOOL
AirOK                   : BOOL
PowerOK                 : BOOL

AxisXDriveFault         : BOOL
AxisZDriveFault         : BOOL
AxisXFollowingError     : REAL
AxisZFollowingError     : REAL

SeqStep                 : DINT
PartPresent             : BOOL
GripConfirmed           : BOOL

ResetCmd                : BOOL
```

### InOut

```text
Fault_EStopDropped      : UDT_Fault
Fault_SafetyChainOpen   : UDT_Fault
Fault_AirLow            : UDT_Fault
Fault_PowerBad          : UDT_Fault
Fault_AxisXDrive        : UDT_Fault
Fault_AxisZDrive        : UDT_Fault
Fault_AxisXFollow       : UDT_Fault
Fault_AxisZFollow       : UDT_Fault
Fault_ZDownTimeout      : UDT_Fault
Fault_GripTimeout       : UDT_Fault
Fault_PartMissing       : UDT_Fault
```

### Outputs

```text
AnyFault                : BOOL
HighestFaultCode        : DINT
FaultResetAllowed       : BOOL
```

### Internal responsibilities

- detect active fault conditions
- latch faults
- assign codes and priority
- choose highest-priority displayed fault
- manage reset qualification

---

## 8.5 AOI_AlarmMgr

### Purpose

Handles non-trip alarms and operator messages.

### Inputs

```text
CycleTimeSlow           : BOOL
MaintenanceDue          : BOOL
AxisXWarning            : BOOL
AxisZWarning            : BOOL
AckCmd                  : BOOL
```

### InOut

```text
Alarm_PartLow           : UDT_Alarm
Alarm_CycleSlow         : UDT_Alarm
Alarm_MaintenanceDue    : UDT_Alarm
Alarm_AxisXWarning      : UDT_Alarm
Alarm_AxisZWarning      : UDT_Alarm
```

### Outputs

```text
AnyAlarm                : BOOL
HighestAlarmCode        : DINT
```

---

# 9. Routine Breakdown

## R00_Init

### Purpose

- initialize retained values if needed
- set startup defaults
- clear non-retentive temp values
- initialize state to `IDLE` or `OFF` depending on machine philosophy

### Typical logic

- on first scan:
  - `MachineSts.State := 10`
  - `Seq.Step := 100`
  - clear all command bits
  - reset temporary outputs

---

## R01_InputMap

### Purpose

Map physical I/O into abstract machine tags.

### Example

```text
MachineCmd.EStopOK           := IO_In_EStopHealthy;
MachineCmd.SafetyOK          := IO_In_SafetyRelayHealthy;
MachineCmd.AirOK             := IO_In_AirPressureOK;

Seq.PartPresent              := IO_In_PartPresent;
Seq.GripConfirmed            := IO_In_GripConfirmed;

AxisXSts.HomeSensor          := IO_In_X_Home;
AxisZSts.HomeSensor          := IO_In_Z_Home;

AxisXSts.ServoReady          := IO_In_X_DriveReady;
AxisZSts.ServoReady          := IO_In_Z_DriveReady;
```

---

## R02_ModeManager

### Purpose

Combine pushbuttons and HMI requests into unified command bits.

### Example

```text
StartCmd := MachineCmd.StartPB OR MachineCmd.HMI_StartCmd;
StopCmd  := MachineCmd.StopPB  OR MachineCmd.HMI_StopCmd;
ResetCmd := MachineCmd.ResetPB OR MachineCmd.HMI_ResetCmd;
```

---

## R03_FaultManager

### Purpose

Call `AOI_FaultMgr` and build machine-level fault summary.

### Notes

- this runs before machine state logic
- fault status must be available before evaluating transitions

---

## R04_MachineState

### Purpose

Call `AOI_MachineStateMgr`.

### Notes

- owns OFF / IDLE / STARTING / HOMING / READY / AUTO_RUN / MANUAL / STOPPING / FAULT / RECOVERY
- global fault override should force `FAULT`

---

## R05_Sequence

### Purpose

Call `AOI_SequenceMgr`.

### Notes

- only active when machine state allows
- does not decide whether the machine is faulted

---

## R06_AxisCommandBuild

### Purpose

Merge requests from machine state and sequence into per-axis commands.

### Example

- if machine in `HOMING`, issue home command to all axes
- if in `MANUAL`, pass jog commands from HMI
- if in `AUTO_RUN`, take move requests from sequence manager

---

## R07_OutputMap

### Purpose

Write abstract outputs to real outputs.

### Example

```text
IO_Out_GripperClose := GripperCloseCmd;

IO_Out_TowerGreen := MachineSts.ReadyToStart AND NOT MachineSts.Faulted;
IO_Out_TowerAmber := AnyAlarm AND NOT MachineSts.Faulted;
IO_Out_TowerRed   := MachineSts.Faulted;
IO_Out_Buzzer     := MachineSts.Faulted;
```

---

## R08_HMI

### Purpose

Populate HMI status words, message IDs, and visible state/step values.

### Example

- state ID to text ID
- current step to operator message
- highest fault/alarm codes to HMI banner

---

## R09_Diagnostics

### Purpose

- transition counters
- cycle counters
- watchdog indicators
- last fault history
- commissioning debug visibility

---

## R10_AxisX_Control / R11_AxisZ_Control

### Purpose

Call one axis AOI per axis.

### Notes

- each axis owns its local motion state
- do not bury axis logic inside sequence logic

---

# 10. Execution Order

This matters.

## Recommended scan order

```text
1. Input Map
2. Command / Mode normalization
3. Fault Manager
4. Machine State Manager
5. Sequence Manager
6. Axis Command Build
7. Axis AOIs
8. Output Map
9. HMI / Diagnostics
```

## Why

If you evaluate state before faults, you create one-scan ambiguity and bad fault behavior.

---

# 11. State Transition Rules

## 11.1 Machine state transitions

| From     | To       | Condition                            |
| -------- | -------- | ------------------------------------ |
| OFF      | IDLE     | EStopOK and SafetyOK                 |
| IDLE     | MANUAL   | Manual mode request                  |
| IDLE     | STARTING | Auto mode request and permissives OK |
| STARTING | HOMING   | axes ready                           |
| HOMING   | READY    | all required axes homed              |
| READY    | AUTO_RUN | cycle start command                  |
| AUTO_RUN | STOPPING | stop or cycle stop                   |
| MANUAL   | STOPPING | stop command                         |
| STOPPING | IDLE     | all axes standstill                  |
| ANY      | FAULT    | AnyFault true                        |
| FAULT    | RECOVERY | reset allowed and reset command      |
| RECOVERY | IDLE     | recovery complete                    |

## 11.2 Axis transitions

| From        | To          | Condition                           |
| ----------- | ----------- | ----------------------------------- |
| DISABLED    | ENABLING    | enable command                      |
| ENABLING    | READY       | drive ready                         |
| READY       | HOMING      | home command                        |
| READY       | JOGGING     | jog command                         |
| READY       | POSITIONING | move command                        |
| READY       | STOPPING    | stop command                        |
| HOMING      | READY       | home done                           |
| POSITIONING | READY       | in position                         |
| JOGGING     | STOPPING    | jog released or stop                |
| STOPPING    | READY       | standstill                          |
| ANY         | FAULT       | drive fault or hard limit violation |
| FAULT       | DISABLED    | reset fault and drive healthy       |

---

# 12. Alarm and Fault Table

Below is a practical alarm/fault register you can paste into a standards page.

## 12.1 Fault severity convention

| Severity | Meaning                | Action                                |
| -------- | ---------------------- | ------------------------------------- |
| 1        | Safety critical trip   | immediate stop, machine FAULT         |
| 2        | Motion critical fault  | stop motion, machine FAULT            |
| 3        | Process critical fault | abort cycle, machine FAULT            |
| 4        | Recoverable fault      | stop process, operator reset required |

## 12.2 Fault table

| Code | Name                        | Severity | Trigger                            | Machine Response             | Reset Rule                    |
| ---- | --------------------------- | -------: | ---------------------------------- | ---------------------------- | ----------------------------- |
| 1001 | E-stop dropped              |        1 | EStopOK false                      | immediate machine FAULT      | restore safety and reset      |
| 1002 | Safety chain open           |        1 | SafetyOK false                     | immediate machine FAULT      | restore safety and reset      |
| 1003 | Control power bad           |        1 | PowerOK false                      | machine FAULT                | power restored and reset      |
| 1101 | Air pressure low            |        3 | AirOK false in run state           | stop cycle, machine FAULT    | air restored and reset        |
| 2001 | Axis X drive fault          |        2 | X drive fault input true           | stop all axes, machine FAULT | drive reset and system reset  |
| 2002 | Axis Z drive fault          |        2 | Z drive fault input true           | stop all axes, machine FAULT | drive reset and system reset  |
| 2101 | Axis X following error high |        2 | abs(following error) > threshold   | stop all axes, machine FAULT | axis healthy, re-home, reset  |
| 2102 | Axis Z following error high |        2 | abs(following error) > threshold   | stop all axes, machine FAULT | axis healthy, re-home, reset  |
| 2201 | Homing timeout              |        3 | homing timer expired               | abort homing, machine FAULT  | clear cause, re-home, reset   |
| 2202 | Z down timeout              |        3 | Z move down timeout                | stop cycle, machine FAULT    | inspect obstruction, reset    |
| 2301 | Part missing                |        4 | expected part absent               | abort cycle, machine FAULT   | restore part and reset        |
| 2302 | Grip confirm timeout        |        3 | grip not confirmed in time         | stop cycle, machine FAULT    | inspect gripper / part, reset |
| 2303 | Unsafe to lower Z           |        2 | safe zone false during Z-down step | stop cycle, machine FAULT    | clear zone, reset             |
| 9001 | Unknown state detected      |        2 | invalid state value                | machine FAULT                | engineering reset             |

## 12.3 Alarm table

| Code | Name            | Priority | Trigger                               | Operator Action            |
| ---- | --------------- | -------: | ------------------------------------- | -------------------------- |
| 3001 | Cycle time slow |        3 | cycle timer exceeds warning threshold | inspect process bottleneck |
| 3002 | Maintenance due |        4 | preventive count exceeded             | schedule maintenance       |
| 3003 | Axis X warning  |        3 | drive warning bit                     | inspect drive              |
| 3004 | Axis Z warning  |        3 | drive warning bit                     | inspect drive              |
| 3005 | Part supply low |        4 | low-part sensor or count              | refill supply              |

---

# 13. HMI Data Contract

## Minimum HMI points

### Commands

- Start
- Stop
- Reset
- Auto Mode
- Manual Mode
- Home
- Cycle Start
- Cycle Stop
- Jog X+
- Jog X-
- Jog Z+
- Jog Z-

### Status

- current machine state
- current sequence step
- axis positions
- axis servo ready
- axis fault
- highest active fault
- highest active alarm
- cycle active
- cycle complete

### Recommended screens

- overview
- manual jog
- fault/alarm banner
- diagnostics
- recovery instructions
- maintenance counters

---

# 14. Recovery Philosophy

## Rules

- do not auto-restart after a fault
- require safety healthy before reset
- after motion faults, require re-home
- clear commanded outputs before re-enable
- operator should see a meaningful fault code, not a vague red light

## Recommended recovery flow

```text
FAULT
→ operator acknowledges
→ verify safety healthy
→ clear outputs
→ reset drives
→ re-enable axes
→ re-home required axes
→ return to IDLE
```

---

# 15. Commissioning Checklist

## Pre-power

- verify I/O mapping
- verify polarity and device naming
- verify safety healthy indication
- verify axis feedback scaling
- verify home sensors and limits

## Dry commissioning

- force state changes without motion
- verify fault banner and alarm messages
- verify transition rules
- verify stop and fault override

## Motion commissioning

- jog each axis at low speed
- confirm sign and direction
- verify home repeatability
- verify in-position logic
- verify following error thresholds

## Sequence commissioning

- run sequence without product
- verify all steps and timers
- simulate missing sensor conditions
- verify fault behavior

## Fault injection

- drive fault
- safety open
- air loss
- grip fail
- timeout fault
- following error fault

---

# 16. Testing Matrix

## Minimum transition test set

| Test ID | Condition                 | Expected Result      |
| ------- | ------------------------- | -------------------- |
| T01     | Power up healthy          | machine enters IDLE  |
| T02     | Auto mode + start         | IDLE to STARTING     |
| T03     | Drives ready              | STARTING to HOMING   |
| T04     | Homing done               | HOMING to READY      |
| T05     | Cycle start               | READY to AUTO_RUN    |
| T06     | Stop pressed              | AUTO_RUN to STOPPING |
| T07     | All axes stopped          | STOPPING to IDLE     |
| T08     | Any fault asserted        | immediate FAULT      |
| T09     | Reset after fault healthy | FAULT to RECOVERY    |
| T10     | Recovery complete         | RECOVERY to IDLE     |

## Minimum process fault test set

| Test ID | Fault                | Expected Result              |
| ------- | -------------------- | ---------------------------- |
| F01     | E-stop open          | immediate FAULT              |
| F02     | Safety relay opens   | immediate FAULT              |
| F03     | Axis X drive fault   | all motion stopped, FAULT    |
| F04     | Axis Z drive fault   | all motion stopped, FAULT    |
| F05     | Grip timeout         | cycle abort, FAULT           |
| F06     | Z-down timeout       | cycle abort, FAULT           |
| F07     | Following error high | immediate motion stop, FAULT |

---

# 17. What Not to Do

## Bad patterns

- one giant ladder routine controlling everything
- sequence bits used as machine mode
- scattered latch/unlatch logic with no owner
- HMI bits directly controlling outputs
- resets that clear faults without qualifying conditions
- no explicit recovery path
- no documented state values

That is how you build a machine nobody trusts.

---

# 18. RAG Notes / Metadata Header

You can add this header above the file if you want it indexed cleanly.

```yaml
title: Studio 5000 Project Skeleton - Multi Axis Machine State Model
type: reference
domain: controls-engineering
platform: studio-5000
topics:
  - finite-state-machine
  - machine-state-model
  - sequence-control
  - axis-control
  - fault-management
  - alarm-management
  - commissioning
  - recovery
tags:
  - plc
  - rockwell
  - studio5000
  - ladder-logic
  - structured-text
  - motion-control
  - machine-automation
purpose: >
  Reference architecture for organizing a multi-axis machine in Studio 5000
  using machine state management, sequence control, axis AOIs, and fault handling.
```

---

# 19. Recommended File Split for Your Standards Site

For your site or RAG, I would split this into these markdown files:

```text
studio5000/
├── studio5000_project_skeleton.md
├── studio5000_udt_reference.md
├── studio5000_aoi_interfaces.md
├── studio5000_state_and_sequence_definitions.md
├── studio5000_fault_alarm_table.md
├── studio5000_commissioning_checklist.md
└── studio5000_testing_matrix.md
```

That makes retrieval cleaner than one giant page.

---

# 20. Interview Summary Version

If someone asks how you structure a Studio 5000 machine, the clean answer is:

> I separate machine mode control, process sequence, axis control, and fault management into different owners. The top-level machine state manager decides whether the machine is idle, homing, ready, running, stopping, faulted, or recovering. A separate sequence manager handles process steps only. Each axis has its own AOI for enable, home, jog, move, stop, and fault handling. A fault manager latches and prioritizes faults and can force the machine into a fault state asynchronously. That structure makes testing, commissioning, and troubleshooting much more reliable.

If you want, I can turn this next into a **set of actual markdown files** or a **downloadable .zip-style documentation package structure** for your Control-System-Tools site.
