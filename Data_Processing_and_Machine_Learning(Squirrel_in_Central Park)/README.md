# SWEN20003 Semester 1, 2026
# Project 1
# Shadow Aliens

## Running Instructions
Open the project in IntelliJ IDEA. Use the pre-configured
run configuration "ShadowAliens". Ensure the following VM
argument is set:
-DgameData=gameData.properties

Alternatively, compile and run from the command line:
javac -cp "bagel-swen20003-2.0.0.jar" -d target/classes
src/main/java/game/*.java
src/main/java/game/entity/*.java
src/main/java/game/powerup/*.java
src/main/java/game/screen/*.java
src/main/java/game/config/*.java

java -DgameData=gameData.properties
-cp "target/classes;bagel-swen20003-2.0.0.jar"
game.ShadowAliens

## Assumptions

* When the player presses N on the last wave, the game
  transitions to the win end screen.
* Hit invincibility applies the same visual effect as the
  shield powerup.
* When a powerup is replaced by a new one, the previous
  powerup's effect is immediately removed.
* Score cannot go below 0.

## AI Statement
I acknowledge the use of Claude to help me debug my code for data analysis.
I used it to analyse sections of my code and to identify potential weaknesses. I used the outputs to revise syntax errors before executing with my full data.
A full record of prompts and outputs is available upon request.

## Code References

* * The project only calls upon the official Bagel engine library provided by the course, without referencing any third-party open-source code or source codes.

## Design Report

### Extension
The Enemy class was the primary area extended from Project 1
to Project 2. In Project 1, Enemy was a single concrete
class with movement logic directly embedded in update(),
and BattleScreen instantiated it directly. Enemy count was
read dynamically from the configuration file by incrementing
an index until no further configuration was found.

To support three new enemy types — regular, strafing, and
shooting — Enemy was converted into an abstract class with
update() declared abstract. Three concrete subclasses were
created: RegularEnemy replicates straight-line movement;
StrafingEnemy adds horizontal bouncing behaviour; and
ShootingEnemy adds periodic shooting via a shoot() method
defined in the abstract Enemy class. An abstract GameObject
class was also introduced as a shared parent for Enemy,
Player, and Projectile, extracting common fields and
getBoundingBox() to eliminate code duplication.

An alternative approach would have been to keep Enemy as a
single class with a type field, using if-else statements
inside update() to switch behaviour. While simpler initially,
this would violate the Open-Closed Principle — every new
enemy type would require modifying the existing Enemy class,
increasing complexity and risk of introducing bugs. The
inheritance-based approach avoids this by isolating each
type's behaviour in its own class.


### Outcome
The final Enemy hierarchy demonstrates solid application of
OOP principles. The abstract Enemy class encapsulates all
shared state and behaviour, while each subclass is solely
responsible for its own update() logic, following the Single
Responsibility Principle. BattleScreen continues to call
e.update() and e.draw() polymorphically without knowing the
specific subtype, demonstrating effective use of polymorphism.

Adding a new enemy type now requires only creating a new
subclass of Enemy and adding one case to the switch statement
in BattleScreen.spawnEnemies(), with no other changes to
BattleScreen — complying with the Open-Closed Principle.
The introduction of GameObject further reduces duplication
across Player, Enemy, and Projectile, making future game
object additions straightforward. The remaining limitation
is the switch statement in spawnEnemies(), which a factory
pattern could fully eliminate for an even more extensible
design.

## Design Report References

* * This design is based on the Project 2b specification, the official structure of Idea, and the official user manual of the Bagel engine.