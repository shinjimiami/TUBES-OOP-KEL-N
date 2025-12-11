# 📋 DAFTAR ASSET PLACEHOLDER YANG SUDAH DIBUAT

Semua placeholder sudah dibuat! Sekarang kamu bisa replace dengan gambar custom kamu sendiri.

## ✅ SUDAH ADA (Asset Original)

### Chef Sprites
- ✅ Kirby (8 sprites) - `/chef/KIRBY_*.png`
- ✅ Waddle Dee (8 sprites) - `/chef/WADDLE DEE_*.png`

### Ingredients (Semua State)
- ✅ Bun, Meat, Cheese, Lettuce, Tomato - `/items/ingredients/`
  - Raw, Chopped, Cooking, Cooked, Burned states

### Kitchen Utensils
- ✅ Plate & Frying Pan - `/items/kitchen_utensils/`

### Station Base
- ✅ 7 stations dasar - `/stations/`

### Menu
- ✅ start.png (Main Menu)

---

## 🆕 BARU DIBUAT (Placeholder - Perlu Diganti!)

### 📐 TILES (3 files) - `/tiles/`
**PRIORITAS TINGGI** - Ini untuk lantai dan dinding dapur
```
tiles/floor.png          → Lantai dapur (64x64)
tiles/wall.png           → Dinding dapur (64x64)
tiles/counter.png        → Counter/meja (64x64)
```

### 🍳 COOKING STATION STATES (17 files) - `/stations/cooking/`
**PRIORITAS TINGGI** - Visual panci di kompor dengan ingredient
```
stations/cooking/pan_empty.png              → Panci kosong di kompor (128x128)

stations/cooking/pan_meat_raw.png           → Daging mentah di panci
stations/cooking/pan_meat_cooking.png       → Daging sedang dimasak (~)
stations/cooking/pan_meat_cooked.png        → Daging matang (+)
stations/cooking/pan_meat_burned.png        → Daging gosong (X)

stations/cooking/pan_cheese_raw.png         → Keju mentah di panci
stations/cooking/pan_cheese_cooking.png     → Keju sedang dimasak (~)
stations/cooking/pan_cheese_cooked.png      → Keju matang (+)
stations/cooking/pan_cheese_burned.png      → Keju gosong (X)

stations/cooking/pan_lettuce_raw.png        → Lettuce mentah di panci
stations/cooking/pan_lettuce_cooking.png    → Lettuce sedang dimasak (~)
stations/cooking/pan_lettuce_cooked.png     → Lettuce matang (+)
stations/cooking/pan_lettuce_burned.png     → Lettuce gosong (X)

stations/cooking/pan_tomato_raw.png         → Tomat mentah di panci
stations/cooking/pan_tomato_cooking.png     → Tomat sedang dimasak (~)
stations/cooking/pan_tomato_cooked.png      → Tomat matang (+)
stations/cooking/pan_tomato_burned.png      → Tomat gosong (X)
```

### 🔪 CUTTING STATION STATES (9 files) - `/stations/cutting/`
**PRIORITAS TINGGI** - Visual ingredient di talenan
```
stations/cutting/board_empty.png            → Talenan kosong (128x128)

stations/cutting/board_meat_raw.png         → Daging mentah di talenan
stations/cutting/board_meat_chopped.png     → Daging terpotong (*)

stations/cutting/board_cheese_raw.png       → Keju mentah di talenan
stations/cutting/board_cheese_chopped.png   → Keju terpotong (*)

stations/cutting/board_lettuce_raw.png      → Lettuce mentah di talenan
stations/cutting/board_lettuce_chopped.png  → Lettuce terpotong (*)

stations/cutting/board_tomato_raw.png       → Tomat mentah di talenan
stations/cutting/board_tomato_chopped.png   → Tomat terpotong (*)
```

### 🍔 ASSEMBLY STATION STATES (10 files) - `/stations/assembly/`
**PRIORITAS SEDANG** - Visual burger di meja rakit (optional, bisa pakai system overlay)
```
stations/assembly/plate_empty.png                   → Piring kosong (128x128)
stations/assembly/plate_bun.png                     → Piring + Bun
stations/assembly/plate_bun_meat.png                → Bun + Meat
stations/assembly/plate_bun_meat_cheese.png         → + Cheese
stations/assembly/plate_bun_meat_lettuce.png        → + Lettuce
stations/assembly/plate_bun_meat_tomato.png         → + Tomato
stations/assembly/plate_classic.png                 → Classic burger lengkap
stations/assembly/plate_cheeseburger.png            → Cheeseburger lengkap
stations/assembly/plate_blt.png                     → BLT burger lengkap
stations/assembly/plate_deluxe.png                  → Deluxe burger lengkap
```

### 📱 MENU BACKGROUNDS (4 files) - `/menu/`
**PRIORITAS SEDANG** - Untuk UI menu yang lebih bagus
```
menu/how_to_play.png     → How to Play screen (1024x768)
menu/stage_select.png    → Stage selection screen (1024x768)
menu/win_screen.png      → Win screen background (1024x768)
menu/lose_screen.png     → Lose screen background (1024x768)
```

### 🎨 UI ELEMENTS (4 files) - `/ui/`
**PRIORITAS RENDAH** - Optional, saat ini pakai rendering manual
```
ui/order_ticket.png      → Background order ticket (300x150)
ui/timer_bar.png         → Timer bar untuk order (200x30)
ui/score_panel.png       → Panel score display (250x100)
ui/chef_indicator.png    → Indicator chef aktif (64x64)
```

### ⭕ FLOOR ITEMS (1 file) - `/items/floor/`
**OPTIONAL** - Bisa reuse sprite ingredient dengan outline
```
items/floor/indicator_circle.png    → Circle indicator (64x64)
```

---

## 📝 CARA REPLACE PLACEHOLDER

1. **Buka folder** `/app/src/main/resources/`

2. **Navigasi ke folder** yang ingin kamu ganti (contoh: `stations/cooking/`)

3. **Replace file PNG** dengan gambar custom kamu
   - **PENTING:** Gunakan nama file yang SAMA PERSIS
   - Contoh: `pan_meat_cooking.png` → ganti dengan gambar kamu tapi tetap nama `pan_meat_cooking.png`

4. **Ukuran yang disarankan:**
   - Tiles: **64x64 px**
   - Station states: **128x128 px**
   - Menu backgrounds: **1024x768 px** atau sesuai screen size
   - UI elements: Sesuai ukuran yang tertera

5. **Format:** PNG dengan transparency (RGBA) lebih bagus

---

## 🎯 PRIORITAS KERJA

### TINGGI (Wajib untuk gameplay visual yang bagus):
1. ✅ **Tiles** → Lantai & dinding dapur
2. ✅ **Cooking Station States** → Panci dengan ingredient
3. ✅ **Cutting Station States** → Talenan dengan ingredient

### SEDANG (Buat UI lebih cantik):
4. ✅ **Menu Backgrounds** → Semua screen menu
5. ✅ **Assembly Station States** → Visual burger (bisa skip, pakai overlay)

### RENDAH (Optional):
6. ✅ **UI Elements** → Panel dan indicator
7. ✅ **Floor Items** → Indicator lantai

---

## 💡 TIPS

- **Untuk piring di station:** Kamu TIDAK perlu bikin asset khusus, game akan render piring secara otomatis di atas station
- **Untuk ingredient di inventory chef:** Juga auto-render, pakai sprite ingredient yang sudah ada
- **Station base** (cooking_station.png, cutting_station.png, dll) sudah ada, ini untuk tampilan station kosong
- **Station states** (pan_meat_cooking.png, board_lettuce_raw.png, dll) adalah OVERLAY yang muncul saat ada item di station tersebut

---

## 🚀 TESTING

Setelah replace placeholder dengan gambar custom:
1. Run game: `./gradlew run`
2. Cek apakah gambar muncul dengan benar
3. Kalau ada yang error/tidak muncul, cek console log

---

## 📂 LOKASI FILE

Semua ada di: `/app/src/main/resources/`

Happy creating! 🎨✨
