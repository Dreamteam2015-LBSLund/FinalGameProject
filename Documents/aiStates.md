### AttackState
Använd existerande soldater för att attackera.
- om soldater inte finns: MakeSoldierState
- om motståndarna är döda: Game Over

### MakeSoldiersState
Gör nya soldater.
- Om ett antal soldater finns: AttackState
- Om ArmyBarrack inte finns: MakeFactoryState
- Om resurser inte finns: GatherResourceState

### MakeFactoryState 
vilken fabrikstyp?  
Konstruera en fabrik.
- Om fabriken är färdig: Förra tillståndet
- Om resurser inte finns: GatherResourceState

### GatherResourceState 
vilken resurs? till vem?  
Samla någon resurs åt staden. (Resurser som finns på kartan eller färdiga i fabriker eller andra inventories.)
- Om klar: förra tillståndet
- Om resursen inte finns: MakeResourceState

### MakeResourceState 
vilken resurs?  
Konstruera någon resurs i fabriker. (Resurser som måste konstrueras i en fabrik, som inte finns nån annanstans.)
- Om klar: förra tillståndet.
- Om fabrik inte finns: MakeFactoryState
