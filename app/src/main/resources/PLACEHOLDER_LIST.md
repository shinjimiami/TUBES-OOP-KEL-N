# Asset Placeholder List

Daftar asset yang perlu ditambahkan. Placeholder sudah dibuat dengan generate_all_placeholders.py

## Tiles (Lantai & Dinding)
- [ ] `tiles/floor.png` - Lantai dapur
- [ ] `tiles/wall.png` - Dinding dapur
- [ ] `tiles/counter.png` - Counter/meja kerja

## Stations (sudah ada semua)
- [x] `stations/ingredient_storage.png` - Tempat penyimpanan bahan
- [x] `stations/plate_storage.png` - Tempat piring
- [x] `stations/cutting_station.png` - Meja potong
- [x] `stations/cooking_station.png` - Kompor
- [x] `stations/assembly_station.png` - Meja rakit burger
- [x] `stations/serving_counter.png` - Counter penyajian
- [x] `stations/washing_station.png` - Tempat cuci/trash

## Ingredients - Raw State
- [x] `items/ingredients/bun_raw.png`
- [x] `items/ingredients/meat_raw.png`
- [x] `items/ingredients/cheese_raw.png`
- [x] `items/ingredients/lettuce_raw.png`
- [x] `items/ingredients/tomato_raw.png`

## Ingredients - Chopped State (*)
- [x] `items/ingredients/meat_chopped.png`
- [x] `items/ingredients/cheese_chopped.png`
- [x] `items/ingredients/lettuce_chopped.png`
- [x] `items/ingredients/tomato_chopped.png`

## Ingredients - Cooking State (~)
- [x] `items/ingredients/meat_cooking.png`
- [x] `items/ingredients/cheese_cooking.png`
- [x] `items/ingredients/lettuce_cooking.png`
- [x] `items/ingredients/tomato_cooking.png`

## Ingredients - Cooked State (+)
- [x] `items/ingredients/meat_cooked.png`
- [x] `items/ingredients/cheese_cooked.png`
- [x] `items/ingredients/lettuce_cooked.png`
- [x] `items/ingredients/tomato_cooked.png`

## Ingredients - Burned State (X)
- [x] `items/ingredients/meat_burned.png`
- [x] `items/ingredients/cheese_burned.png`
- [x] `items/ingredients/lettuce_burned.png`
- [x] `items/ingredients/tomato_burned.png`

## Kitchen Utensils
- [x] `items/kitchen_utensils/plate.png` - Piring kosong
- [x] `items/kitchen_utensils/frying_pan.png` - Panci goreng

## NEW - Cooking Station States (Panci di Kompor dengan Ingredient)
**PERLU DIBUAT - Untuk visual saat ingredient sedang dimasak di kompor**

### Meat in Pan on Stove
- [ ] `stations/cooking/pan_empty.png` - Panci kosong di kompor
- [ ] `stations/cooking/pan_meat_raw.png` - Daging mentah di panci
- [ ] `stations/cooking/pan_meat_cooking.png` - Daging sedang dimasak (~)
- [ ] `stations/cooking/pan_meat_cooked.png` - Daging matang (+)
- [ ] `stations/cooking/pan_meat_burned.png` - Daging gosong (X)

### Cheese in Pan on Stove
- [ ] `stations/cooking/pan_cheese_raw.png`
- [ ] `stations/cooking/pan_cheese_cooking.png`
- [ ] `stations/cooking/pan_cheese_cooked.png`
- [ ] `stations/cooking/pan_cheese_burned.png`

### Lettuce in Pan on Stove
- [ ] `stations/cooking/pan_lettuce_raw.png`
- [ ] `stations/cooking/pan_lettuce_cooking.png`
- [ ] `stations/cooking/pan_lettuce_cooked.png`
- [ ] `stations/cooking/pan_lettuce_burned.png`

### Tomato in Pan on Stove
- [ ] `stations/cooking/pan_tomato_raw.png`
- [ ] `stations/cooking/pan_tomato_cooking.png`
- [ ] `stations/cooking/pan_tomato_cooked.png`
- [ ] `stations/cooking/pan_tomato_burned.png`

## NEW - Cutting Station States (Ingredient di Meja Potong)
**PERLU DIBUAT - Untuk visual saat ingredient sedang dipotong**

- [ ] `stations/cutting/board_empty.png` - Talenan kosong
- [ ] `stations/cutting/board_meat_raw.png` - Daging mentah di talenan
- [ ] `stations/cutting/board_meat_chopped.png` - Daging terpotong
- [ ] `stations/cutting/board_cheese_raw.png`
- [ ] `stations/cutting/board_cheese_chopped.png`
- [ ] `stations/cutting/board_lettuce_raw.png`
- [ ] `stations/cutting/board_lettuce_chopped.png`
- [ ] `stations/cutting/board_tomato_raw.png`
- [ ] `stations/cutting/board_tomato_chopped.png`

## NEW - Assembly Station States (Burger di Meja Rakit)
**PERLU DIBUAT - Untuk visual burger yang sedang dirakit**

### Simple Combinations
- [ ] `stations/assembly/plate_empty.png` - Piring kosong di meja
- [ ] `stations/assembly/plate_bun.png` - Piring + Bun
- [ ] `stations/assembly/plate_bun_meat.png` - Bun + Meat
- [ ] `stations/assembly/plate_bun_meat_cheese.png` - + Cheese
- [ ] `stations/assembly/plate_bun_meat_lettuce.png` - + Lettuce
- [ ] `stations/assembly/plate_bun_meat_tomato.png` - + Tomato

### Complex Burgers
- [ ] `stations/assembly/plate_classic.png` - Classic burger (Bun+Meat+)
- [ ] `stations/assembly/plate_cheeseburger.png` - Cheeseburger
- [ ] `stations/assembly/plate_blt.png` - BLT burger
- [ ] `stations/assembly/plate_deluxe.png` - Deluxe burger

## NEW - Floor Items (Item yang Terjatuh/Dilempar)
**OPTIONAL - Bisa pakai item sprite biasa dengan indicator outline**

Untuk item yang dilempar ke lantai, kita bisa reuse sprite ingredient yang ada, 
tapi jika ingin special indicator:
- [ ] `items/floor/indicator_circle.png` - Circle indicator untuk item di lantai

## Menu Assets
- [x] `menu/start.png` - Main menu background
- [ ] `menu/how_to_play.png` - How to play screen background
- [ ] `menu/stage_select.png` - Stage selection background
- [ ] `menu/win_screen.png` - Win screen background
- [ ] `menu/lose_screen.png` - Lose screen background

## UI Elements
- [ ] `ui/order_ticket.png` - Background untuk order ticket
- [ ] `ui/timer_bar.png` - Timer bar untuk order
- [ ] `ui/score_panel.png` - Panel untuk score display
- [ ] `ui/chef_indicator.png` - Indicator chef yang aktif

## Chef Sprites (sudah ada semua untuk Kirby & Waddle Dee)
- [x] All Kirby sprites (8 directions)
- [x] All Waddle Dee sprites (8 directions)

---

## Catatan Penting:

### Yang WAJIB dibuat custom:
1. **Tiles** (floor, wall, counter) - Untuk tampilan dapur
2. **Cooking Station States** - Visual panci dengan ingredient di kompor
3. **Cutting Station States** - Visual ingredient di talenan
4. **Menu Backgrounds** - Untuk UI menu yang lebih bagus

### Yang OPTIONAL:
1. Assembly Station States - Bisa tetap pakai system overlay ingredient
2. Floor Items - Bisa reuse sprite ingredient dengan indicator outline
3. UI Elements - Saat ini masih menggunakan rendering manual

### Yang sudah SELESAI:
1. Semua ingredient states (raw, chopped, cooking, cooked, burned)
2. Kitchen utensils (plate, pan)
3. Station sprites dasar
4. Chef sprites (Kirby & Waddle Dee)
