# Presenter Script — Kyaw Min Thu
### How to use: Glance → speak → look up. Do NOT read out loud. Use **key lines** as anchors.

---

## SLIDE 1 — TITLE

> Hi, I'm Kyaw Min Thu.
> I'm a controls engineer working on multi-domain industrial systems — where control logic interacts directly with mechanical systems, gas flow, and physical processes.
> My focus is diagnosing failures that are not obvious, especially when they cross multiple domains.

**[Pause — look at audience]**

---

## SLIDE 2 — WHAT I DO

> When systems fail, the first assumption is usually that the PLC or software is the problem.
> But in my experience, that's often not the case.
> Most failures originate in the physical system — pressure, flow, or mechanical behavior — and the control system is just reacting.

**[Slow down]**
> So my approach is to identify which domain is actually failing — before making changes.

---

## SLIDE 3 — BACKGROUND (OFFSHORE)

> I started working on offshore hydraulic systems — specifically top drive systems.
> These involve hydraulic pressure, mechanical load, electrical control, and safety systems.
> In that environment, you cannot guess — you must isolate the problem domain first.

**[Key line]**
> "Correct control logic does not guarantee correct system behavior."

---

## SLIDE 4 — TRANSITION

> That experience shaped how I approach troubleshooting.
> I don't start by changing logic.
> I start by understanding system behavior and identifying the responsible domain.

---

## SLIDE 5 — RCA PHILOSOPHY

> My approach to root cause analysis is structured.
> I separate symptoms from root causes, and I validate each domain independently.

**[Point to slide while speaking]**
> Control... Electrical... Mechanical / Process.

**[Key line]**
> "A system can behave correctly from the control perspective — and still fail due to process conditions."

---

## SLIDE 6 — RCA FRAMEWORK

> I follow a consistent process:

- Define the problem
- Isolate domains
- Collect data
- Validate root cause
- Fix and prevent

**[Pause]**
> The most important step is data collection.
> Without data, troubleshooting becomes guesswork.

---

## SLIDE 7 — CASE 1: PROBLEM (Fire System)

> This is a fire training simulator — it uses gas, air, and ignition control.

**[Continue]**
> The issue was intermittent ignition failure.
> Sometimes it started immediately. Sometimes it required multiple attempts.

**[Key point]**
> From the control system perspective — everything looked correct.

---

## SLIDE 8 — INVESTIGATION

> We verified the electrical signals:
> - Burner management trigger ✓
> - Gas valve operation ✓
> - Air valve operation ✓
>
> Everything was working correctly from the control side.

**[Conclusion]**
> So control logic was not the root cause.

---

## SLIDE 9 — ROOT CAUSE ⚠️ SLOW DOWN HERE

> The issue was insufficient air pressure during ignition.
> The air filter was clogged — which restricted airflow.

**[Continue]**
> The compressor rebuilt pressure over time — which made the failure appear random.

**[Key line — PAUSE after]**
> "Logic errors don't self-recover. Process issues do."

---

## SLIDE 10 — SOLUTION

> We replaced the air filter.
> But more importantly — we added a pressure transmitter to monitor the air system.

**[Continue]**
> We also added SCADA monitoring and a maintenance schedule.

**[Key line]**
> "Fixing the issue is not enough — you need to make the condition visible."

---

## SLIDE 11 — LESSON (Case 1)

> The key takeaway is simple:
> If you don't have instrumentation — you don't have visibility.

---

## SLIDE 12 — CASE 2: PROBLEM (Intermittent Shutdown)

> In this case, the system shut down intermittently.
> It happened once a day — or a few times per week.

**[Continue]**
> There was no logging. The issue could not be reproduced.

---

## SLIDE 13 — STRATEGY

> At that point, troubleshooting was not possible.

**[Pause]**
> So the first step was not debugging — it was creating visibility.

**[Key line]**
> "If you can't see the event — you can't solve it."

---

## SLIDE 14 — IMPLEMENTATION

> I built a 24/7 monitoring system using Python.

**[Continue]**
> It tracked:
> - Stop conditions
> - Permissives
> - Interlocks
> - Commands
>
> And sent real-time alerts through Slack.

---

## SLIDE 15 — ROOT CAUSE (Case 2)

> We found that a manual button was being intermittently triggered by an associate.

**[Continue]**
> This was not visible in standard PLC trending.

**[Key line]**
> "Standard tools were not enough — so I created a monitoring layer."

---

## SLIDE 16 — COMPARISON

> These two cases represent different failure types:
> - Case 1: process issue → solved with instrumentation
> - Case 2: intermittent issue → solved with monitoring

**[Key line]**
> "Different problems require different strategies — but both require real data."

---

## SLIDE 17 — RELEVANCE TO VELO3D

> Systems like Velo3D have similar challenges:
> - Gas flow
> - Thermal behavior
> - Motion control

**[Continue]**
> Failures may not originate in control logic — they can come from physical interactions.

**[Key line]**
> "The key is identifying which domain is responsible — before making changes."

---

## SLIDE 18 — CLOSING

**[Slow down]**
> What I bring is strong root cause analysis capability, cross-domain understanding, and a structured, data-driven approach.

**[Pause]**
> I focus on systems where failures are not obvious — and where real data makes the difference.

---

## KEY LINES TO MEMORIZE (anchors)

| Slide | Key Line |
|-------|----------|
| 3 | Correct control logic does not guarantee correct system behavior. |
| 9 | Logic errors don't self-recover. Process issues do. |
| 10 | Fixing the issue is not enough — make the condition visible. |
| 13 | If you can't see the event, you can't solve it. |
| 16 | Different problems require different strategies — but both require real data. |

---

## TIPS FOR DELIVERY

- Speak **slower** than you think you need to
- After every **key line** — pause 2 seconds, look at the audience
- Don't memorize word-for-word — know the **story**, then talk
- It's okay to glance at your notes — just **look up** when saying key lines
