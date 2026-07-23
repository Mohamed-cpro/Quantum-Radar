# Quantum Radar (QuRadar)

A simple, extensible Java traffic radar system. It takes observations (as if
reported by a physical radar), checks them against a set of pluggable
rules, and issues fines for any violations found.

## Overview

`QuRadar` receives an `Observation` for each car that passes: plate number,
date, car type (`PRIVATE`, `TRUCK`, `BUS`), speed, and seatbelt status.

In a real deployment, the physical radar hardware would use an AI
perception pipeline to produce that data — e.g. a YOLO-style detector to
classify the vehicle, an OCR/License-Plate-Recognition model to read the
plate, a laser/doppler sensor for speed, and a small CNN classifier to
detect whether the seatbelt is fastened. `QuRadar` doesn't care how the
data was produced — it just consumes `Observation` objects.

`QuRadar` holds no hard-coded traffic laws itself. Instead, it holds a list
of `Rule` objects. Each `Rule` knows how to check one observation and
return zero or one `Violation`. This keeps `QuRadar` open for extension
(add new rules any time) but closed for modification (never edit
`QuRadar` to add a rule).

## Project structure

```
src/
├── Main.java                 # console demo — you play the role of the radar
├── model/
│   ├── Car.java               # plate number + car type
│   ├── CarType.java            # PRIVATE, TRUCK, BUS
│   ├── Observation.java        # one radar reading (car, date, speed, seatbelt)
│   ├── Violation.java          # a single broken rule (rule name, detail, fee)
│   └── Fine.java                # a car's violations bundled into a bill
├── rules/
│   ├── Rule.java                # interface — the extension point
│   ├── SpeedRule.java            # max speed per car type
│   └── SeatbeltRule.java         # seatbelt must be fastened
└── radar/
    └── QuRadar.java              # orchestrates rules, issues + tracks fines
```

## How to build and run

From inside the `src` folder:

```bash
javac -d out $(find . -name "*.java")
java -cp out Main
```

Or, if running from an IDE / Code Runner, make sure the code executes in
an **interactive terminal**, not a read-only output panel — the program
reads input from the console.

### Using it

```
Enter observation (or type 'exit' to stop):
Plate number: ABC1234
Car type (PRIVATE, TRUCK, BUS): PRIVATE
Speed: 94
Seatbelt fastened? (yes/no): no
```

This immediately prints a fine (if there are violations):

```
Traffic for car ABC1234
Total amount: 400 EGP
Violations:
- speed of 94 exceeded max allowed 80 : 300 EGP
- Seatbelt not fastned : 100 EGP
```

Keep entering plates, or type `exit` to stop. At the end, it prints two
summaries:

```
=== All fines (total per plate) ===
ABC1234 : 400 EGP

=== Violated rules (count) ===
Speed Limit -> 1
Seatbelt -> 1
```

## Rules included by default

| Rule          | Applies to | Limit / condition   | Fee     |
|---------------|-----------|----------------------|---------|
| `SpeedRule`   | TRUCK     | speed > 60            | 300 EGP |
| `SpeedRule`   | PRIVATE   | speed > 80            | 300 EGP |
| `SpeedRule`   | BUS       | speed > 70             | 300 EGP |
| `SeatbeltRule`| any       | seatbelt not fastened  | 100 EGP |

These are registered in `Main.java`:

```java
radar.addRule(new SpeedRule(CarType.TRUCK, 60, 300));
radar.addRule(new SpeedRule(CarType.PRIVATE, 80, 300));
radar.addRule(new SpeedRule(CarType.BUS, 70, 300));
radar.addRule(new SeatbeltRule(100));
```

## Adding a new rule

No changes to `QuRadar` are needed. Just implement `Rule`:

```java
public class NoOvertakingRule implements Rule {
    @Override
    public Violation evaluate(Observation observation) {
        if (/* condition */) {
            return new Violation("No Overtaking", "overtook on the shoulder", 250);
        }
        return null;
    }
}
```

Then register it:

```java
radar.addRule(new NoOvertakingRule());
```

## Key API

- `radar.processObservation(Observation)` — evaluates all rules against one
  observation, prints and returns a `Fine` if there are violations (or
  `null` if the car is compliant).
- `radar.getAllPossibleFines()` — `Map<plateNumber, totalAmount>` across
  every fine issued so far.
- `radar.getAllViolatedRules()` — `Map<ruleName, count>` of how many times
  each rule has been broken.
