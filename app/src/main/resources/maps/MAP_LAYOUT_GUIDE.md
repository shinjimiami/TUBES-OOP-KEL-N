# Map Layout Guide

## Map D - Complex Kitchen Layout

**Dimensions:** 1200x880 pixels (15 tiles wide x 11 tiles tall @ 80px per tile)

### Layout Description
Berdasarkan screenshot yang diberikan, layout ini memiliki:

```
█ █ █ █ █ █ █ █ █ █ █ █ █ █ █
█ M █ █ █ █ K K K K K K █ █ █
█ C █ R █ X P █ █ █ P █ R █ █
█ B █ █ █ █ · · · · · █ █ █ █
█ T █ █ K █ · · · · · █ K █ █
█ L █ █ █ █ · · · · · █ █ █ █
█ █ █ █ █ X P █ █ █ P █ █ █ █
█ █ █ K K K K K K S K K K █ █
█ D D █ █ █ █ █ █ █ █ █ █ █ █
X · · · · · · · · · · · · · X
█ █ █ █ █ █ █ █ █ █ █ █ █ █ █
```

### Legend
- `█` = Wall (Dinding coklat)
- `·` = Floor (Lantai checkerboard kuning/krem)
- `M` = Meat Box (Kotak daging)
- `C` = Cheese Box (Kotak keju)
- `B` = Bun Box (Kotak roti)
- `T` = Tomato Box (Kotak tomat)
- `L` = Lettuce Box (Kotak selada)
- `P` = Plate Station (Stasiun piring biru)
- `S` = Stove (Kompor)
- `K` = Counter (Meja kerja kayu)
- `D` = Delivery Window (Jendela pengiriman)
- `X` = Door (Pintu biru)
- `R` = Tree Decoration (Pohon pink)

### Key Features

#### 1. Ingredient Storage (Kiri Atas)
- Vertikal arrangement dari atas ke bawah:
  - Row 2, Col 1: Meat Box
  - Row 3, Col 1: Cheese Box
  - Row 4, Col 1: Bun Box
  - Row 5, Col 1: Tomato Box
  - Row 6, Col 1: Lettuce Box

#### 2. Working Area (Tengah)
- Large open floor space (rows 3-6, cols 6-11)
- Allows multiple chefs to move freely
- Central area for gameplay

#### 3. Plate Stations
- Row 3, Col 7: Left plate station
- Row 3, Col 11: Right plate station
- Row 7, Col 7: Bottom left plate station
- Row 7, Col 11: Bottom right plate station

#### 4. Counter & Stove Area (Bawah)
- Row 8: Long counter with stove in center
- Col 4-12: Counter tiles
- Col 10: Stove/cooking station

#### 5. Delivery Area (Kiri Bawah)
- Row 9, Cols 2-3: Delivery window
- Service counter for completed orders

#### 6. Entrances/Exits
- Row 3, Col 6: Door (upper middle)
- Row 7, Col 6: Door (lower middle)
- Row 10, Cols 1 & 15: Side exits

#### 7. Decorations
- Row 3, Col 4: Pink tree
- Row 3, Col 12: Pink tree
- Adds visual interest to the kitchen

### Tile Coordinates (0-indexed)
If implementing programmatically:

```
Width: 15 tiles (0-14)
Height: 11 tiles (0-10)

Meat Box: (1, 1)
Cheese Box: (1, 2)
Bun Box: (1, 3)
Tomato Box: (1, 4)
Lettuce Box: (1, 5)

Plate Stations: (6, 2), (10, 2), (6, 6), (10, 6)
Stove: (9, 7)
Delivery Window: (1-2, 8)
```

### Implementation Notes

1. **Asset Loading**: Replace `maps/map_d.png` placeholder with actual artwork
2. **Collision Map**: Need to define walkable vs non-walkable tiles
3. **Station Positions**: Coordinates for interactive stations
4. **Spawn Points**: Chef starting positions (suggested: near floor center)
5. **Camera**: May need to adjust viewport for 1200x880 map

### Comparison with Map C
- Map C: Simpler layout, single burger preparation area
- Map D: Complex multi-station setup with dedicated zones
- Map D requires more coordination between chefs
- Better for 2-player co-op gameplay
