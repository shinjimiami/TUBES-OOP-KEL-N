# ⏰ Quick Guide: Custom Timer Background

## 🎯 Cara Gampang Ganti Timer Background

### **3 Langkah Mudah:**

1. **Buat gambar timer** (120×35 pixels, PNG)
2. **Save ke:** `app/src/main/resources/ui/timer_background.png`
3. **Done!** Rebuild & run game

---

## 📐 Spesifikasi Gambar

```
Filename: timer_background.png
Location: app/src/main/resources/ui/
Size:     120 pixels × 35 pixels
Format:   PNG (dengan transparency)
Position: Pojok kiri atas layar
```

---

## 🎨 Template Design

### **Area Timer:**
```
┌──────────────────────────┐  ← 120px
│                          │
│      [  TEXT  ]          │  ← Text akan muncul di sini
│                          │
└──────────────────────────┘
      ↑
     35px
```

### **Text Safe Area:**
Pastikan area tengah (80×20px) cukup terang/gelap untuk text:
- Text akan tampil dengan warna: **Green / Yellow / Red**
- Font: **daydream PLAIN 18px**
- Position: **Centered**

---

## 💡 Tips Design

✅ **DO:**
- Gunakan background gelap (agar text terlihat)
- Tambahkan border/frame
- Gunakan transparency
- Test dengan semua warna text

❌ **DON'T:**
- Background terlalu ramai
- Text area tertutup gambar
- Ukuran salah (harus 120×35)
- Format JPG (gunakan PNG)

---

## 🖼️ Contoh Style

### **1. Simple Box**
```
┌───────────────────────┐
│       2:45            │  Black + gold border
└───────────────────────┘
```

### **2. Pixel Art**
```
╔═══════════════════════╗
║   ⏰   2:45   ⏰     ║  Retro style
╚═══════════════════════╝
```

### **3. Modern**
```
┌───────────────────────┐
│ ⏱️  2:45              │  Clean minimal
└───────────────────────┘
```

---

## 🔧 Testing

**Kalau gambar tidak muncul:**
- Cek nama file: `timer_background.png` (lowercase!)
- Cek lokasi: `app/src/main/resources/ui/`
- Cek size: Harus **exactly** 120×35 pixels
- Rebuild: `./gradlew build`

**Game akan otomatis pakai default box kalau:**
- File tidak ada
- File corrupt/error
- Size salah

---

## 📂 File Structure

```
TUBES-OOP-KEL-N/
└── app/src/main/resources/
    └── ui/
        └── timer_background.png  ← Taruh di sini!
```

---

## 🚀 Quick Start

```bash
# 1. Design gambar (120×35 PNG)
# 2. Save ke folder ui/
cp my_timer.png app/src/main/resources/ui/timer_background.png

# 3. Build & Run
./gradlew build
./gradlew run
```

---

## 📸 Preview In-Game

**Default (no custom image):**
```
Game Screen:
┌─────────────────────────────┐
│ [⬛ 2:45]  [ORDER] [ORDER] │  ← Default box
```

**With Custom Image:**
```
Game Screen:
┌─────────────────────────────┐
│ [🎨 2:45]  [ORDER] [ORDER] │  ← Your design!
```

---

## 💾 Backup Template

Kalau mau bikin beberapa versi:

```bash
app/src/main/resources/ui/
├── timer_background.png         ← Active
├── timer_background_v1.png      ← Backup 1
├── timer_background_v2.png      ← Backup 2
└── timer_background_pixel.png   ← Alternative
```

Ganti cukup rename file!

---

## 🎯 Result

✅ Timer background customizable  
✅ Auto-fallback kalau file tidak ada  
✅ Support transparency  
✅ Text always visible  
✅ Build successful!

**Dokumentasi lengkap:** [TIMER_PLACEHOLDER_GUIDE.md](TIMER_PLACEHOLDER_GUIDE.md)

---

**Status:** ✅ Ready!  
**Build:** ✅ Success!  
**Have fun designing! 🎨**
