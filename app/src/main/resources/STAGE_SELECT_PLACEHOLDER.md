# Stage Select Screen Placeholder Guide

## 📸 Main Stage Select Background
**File:** `/menu/stage_select.png`
**Size:** 672×570 px (14 tiles × 11.875 tiles @ 48px/tile)
**Purpose:** Full background for stage selection screen

### Design Elements:
- **Top:** "SELECT STAGE" title text (large, PLAIN font)
- **Center:** Map preview frame/card area
  - Map name display area (e.g., "MAP TYPE C BURGER MAP")
  - Map thumbnail/preview (showing kitchen layout)
  - Decorative frame/border around preview
- **Bottom:** Navigation instructions
  - "USE WS TO NAVIGATE | ENTER/SPACE TO SELECT | ESC TO GO BACK"

### Color Scheme:
- Warm, cozy kitchen theme (browns, creams, pastels)
- Pink ribbon/bow decoration (optional)
- Checkered tablecloth pattern (background)

---

## 🗺️ Map Thumbnails
**Directory:** `/maps/thumbnails/`

### Individual Map Previews:
1. **map_c_thumbnail.png** (Current map)
   - Size: 256×256 px (recommended)
   - Shows: Bird's eye view of burger kitchen layout
   - Elements visible: Stations, counters, walkable areas

### Future Maps (if adding more):
2. **map_a_thumbnail.png** 
3. **map_b_thumbnail.png**
4. **map_d_thumbnail.png**

---

## 🎨 Placeholder Creation Commands

### Using ImageMagick:

```bash
# Main stage select background
convert -size 672x570 xc:'#F5E6D3' \
  -gravity center \
  -fill '#8B4513' -pointsize 48 -font daydream-PLAIN \
  -annotate +0-200 'SELECT STAGE' \
  -fill '#FFE4E1' \
  -draw 'roundrectangle 136,120 536,450 20,20' \
  -fill '#8B4513' -pointsize 24 \
  -annotate +0+0 'MAP TYPE C\nBURGER MAP' \
  -fill '#654321' -pointsize 12 \
  -annotate +0+250 'USE WS TO NAVIGATE | ENTER/SPACE TO SELECT | ESC TO GO BACK' \
  menu/stage_select.png

# Map thumbnail
convert -size 256x256 xc:'#DEB887' \
  -gravity center \
  -fill '#8B4513' -pointsize 36 \
  -annotate +0+0 'MAP\nC' \
  maps/thumbnails/map_c_thumbnail.png
```

### Using Python (PIL):

```python
from PIL import Image, ImageDraw, ImageFont

# Main stage select background
img = Image.new('RGB', (672, 570), '#F5E6D3')
draw = ImageDraw.Draw(img)

# Title
draw.text((336, 50), 'SELECT STAGE', fill='#8B4513', anchor='mm', 
          font=ImageFont.truetype('daydream', 48))

# Map frame
draw.rounded_rectangle([(136, 120), (536, 450)], radius=20, 
                       fill='#FFE4E1', outline='#8B4513', width=3)

# Map name
draw.text((336, 280), 'MAP TYPE C\nBURGER MAP', fill='#8B4513', 
          anchor='mm', font=ImageFont.truetype('daydream', 24), align='center')

# Instructions
draw.text((336, 530), 'USE WS TO NAVIGATE | ENTER/SPACE TO SELECT | ESC TO GO BACK',
          fill='#654321', anchor='mm', font=ImageFont.truetype('daydream', 12))

img.save('menu/stage_select.png')

# Map thumbnail
thumb = Image.new('RGB', (256, 256), '#DEB887')
draw_thumb = ImageDraw.Draw(thumb)
draw_thumb.text((128, 128), 'MAP\nC', fill='#8B4513', anchor='mm',
                font=ImageFont.truetype('daydream', 72), align='center')
thumb.save('maps/thumbnails/map_c_thumbnail.png')
```

---

## 📐 Layout Specifications

### Stage Select Screen Layout:
```
┌─────────────────────────────────────────────┐
│                                             │
│         SELECT STAGE (Title)                │ ← Y: 50px
│                                             │
│    ┌───────────────────────────────┐       │
│    │  [Pink Ribbon Decoration]    │       │ ← Y: 120px
│    │                               │       │
│    │   MAP TYPE C                  │       │
│    │   BURGER MAP                  │       │ ← Y: 200px
│    │                               │       │
│    │  ┌─────────────────────┐     │       │
│    │  │  [Map Thumbnail]    │     │       │ ← Y: 250px
│    │  │   Kitchen Layout    │     │       │
│    │  └─────────────────────┘     │       │
│    │                               │       │
│    └───────────────────────────────┘       │ ← Y: 450px
│                                             │
│  USE WS | ENTER/SPACE | ESC TO GO BACK     │ ← Y: 530px
│                                             │
└─────────────────────────────────────────────┘
```

### Dimensions:
- Screen: 672×570 px
- Title area: Y 0-100
- Map card: 400×330 px, centered (X: 136-536, Y: 120-450)
- Thumbnail: 256×256 px inside card
- Instructions: Y 500-550

---

## 🎯 Integration in Code

The image you uploaded should be saved as:
- **Primary:** `/app/src/main/resources/menu/stage_select.png`
- **Thumbnail:** `/app/src/main/resources/maps/thumbnails/map_c_thumbnail.png`

Current UI.java likely loads it as:
```java
stageSelectBackground = setup("/menu/stage_select");
```

---

## ✨ Design Tips

1. **Frame/Card:** Add decorative frame around map preview
2. **Ribbon:** Pink bow decoration adds charm (like in your image)
3. **Checkered Pattern:** Tablecloth background for kitchen theme
4. **Soft Colors:** Cream, beige, pink pastels
5. **Clear Text:** High contrast text for readability
6. **Icons:** Small icons for navigation hints (arrows, keyboard keys)

