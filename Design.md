Stormlight
=========

## Objective

Create a mod which implements Surgebinding from Brandon Sanderson's *The Stormlight Archives* to Minecraft

### Items
 * Crystal Spheres. Made with glass, and a diamond or emerald. Used to store Stormlight, and Charged variant is created in Thunderstorms. Used item is known as Dun Crystal, and can be recharged. Can be placed as a light source 
 * Shardblade. OP lightsaber, but super rare. Possibly similar to elytra, worldgen castles which are super rare.
 * Shardplate. The OP armor equivalent of the above.
 * Honorblade. A possible alternative to the above shardblade which grants Surgebinding ability. 
 ### Mechanics
* Each surgebinder can be granted two surges
* Surges
  1. Gravitation - Can control gravity. Use nogravity or levitation effect.
  2. Adhesion - Binds stuff together. Stick entities to blocks?
  3. Division - Breaks stuff. Can divide gollems back into blocks
  4. Abrasion - Can manipulate friction. In effect, speed boosting or climbing ability.
  5. Progression - Accelerates growth or heals. Basically bonemeal or regen
  6. Illumination - Can create illusions. Fake mobs or player? Possible structure recreation (with fallingsand)
  7. Transformation - Turn certain objects into other objects. Danger, Will Robinson.
      * Possible transformations: cobble -> stone. obsidian -> lava. 
  8. Transportation - Able to teleport. Maybe just to spawn or between dimensions?
  9. Cohesion - Alter the structure of an object. Unclear as to use
  10. Tension - Alter the stiffness of an object. Unclear, perhaps simply grant haste or maybe decrease tool requirements
* Simply inhaling Stormlight should grant some status effects.
* Inhale Stormlight by clicking gems. 
* Stormlight is used by surges and decreases over time.
* Shardblades should be able to be summoned and dismissed. Dropping a shardblade counts as dismissal
  * Stored in enderchests or as a single boolean in the Capability
### Crafting
Gems are made from glass and a diamond or emerald. Creates tiny spheres (8?).
### Becoming a Surgebinder
Difficult. Perhaps requires certain achievements, as a kind of skill-tree (Oaths). Easy way out is to say Honorblades only.
### Spren
 * Tiny, cube entities of different colors. Spawn randomly? Need more thought, but probably will be "tameable" through something heroic, a nether star or something
### Implementation
Two options:
1. Easy. Honorblades only, stormlight is a status effect
2. Harder. Bonding and some sort of NBT data. A Capability with several fields - a Type field [1-10], power [0-5] and blade stored [0-1] at least.