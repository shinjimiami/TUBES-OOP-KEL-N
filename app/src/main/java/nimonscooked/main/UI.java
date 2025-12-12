package nimonscooked.main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import nimonscooked.entity.Chef;
import nimonscooked.entity.order.Order;

public class UI {
	GamePanel gp;

	public UI(GamePanel gp) {
		this.gp = gp;
	}

	public void drawPauseScreen(Graphics2D g2) {
		g2.setColor(new Color(0, 0, 0, 128));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

		if (gp.pauseOverlayImage != null) {
			g2.drawImage(gp.pauseOverlayImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
		} else {
			drawDefaultPauseMenu(g2);
		}

		int menuWidth = 300;
		int menuHeight = 250;
		int menuX = (gp.screenWidth - menuWidth) / 2;
		int menuY = (gp.screenHeight - menuHeight) / 2;

		String[] options = { "Resume", "Controls", "Quit" };
		int optionY = menuY + 90;
		int optionSpacing = 30;

		g2.setFont(new Font("DayDream", Font.PLAIN, 14));
		for (int i = 0; i < options.length; i++) {
			if (i == gp.pauseSelectedIndex) {
				g2.setColor(new Color(121, 51, 51));
				g2.setFont(new Font("daydream", Font.PLAIN, 16));
				String text = "< " + options[i];
				int textWidth = g2.getFontMetrics().stringWidth(text);
				int textX = menuX + (menuWidth - textWidth) / 2;
				int textY = optionY + (i * optionSpacing);
				g2.drawString(text, textX, textY);
				g2.setStroke(new BasicStroke(4));
				g2.drawLine(textX, textY + 3, textX + textWidth, textY + 3);
			} else {
				g2.setColor(new Color(121, 51, 51));
				g2.setFont(new Font("daydream", Font.PLAIN, 14));
				int textWidth = g2.getFontMetrics().stringWidth(options[i]);
				g2.drawString(options[i], menuX + (menuWidth - textWidth) / 2, optionY + (i * optionSpacing));
			}
		}
	}

	public void drawDefaultPauseMenu(Graphics2D g2) {
		int menuWidth = 300;
		int menuHeight = 250;
		int menuX = (gp.screenWidth - menuWidth) / 2;
		int menuY = (gp.screenHeight - menuHeight) / 2;

		g2.setColor(new Color(30, 30, 30, 220));
		g2.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);

		g2.setColor(new Color(255, 215, 0));
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);

		g2.setColor(new Color(255, 215, 0));
		g2.setFont(new Font("Arial", Font.BOLD, 28));
		FontMetrics fm = g2.getFontMetrics();
		String pausedText = "PAUSED";
		int pausedTextWidth = fm.stringWidth(pausedText);
		g2.drawString(pausedText, menuX + (menuWidth - pausedTextWidth) / 2, menuY + 40);
	}

	public void drawControlsScreen(Graphics2D g2) {
		int boxWidth = 500;
		int boxHeight = 360;
		int boxX = (gp.screenWidth - boxWidth) / 2;
		int boxY = (gp.screenHeight - boxHeight) / 2;

		g2.setColor(new Color(30, 30, 30, 220));
		g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

		g2.setColor(new Color(255, 215, 0));
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

		g2.setColor(new Color(255, 215, 0));
		g2.setFont(new Font("Arial", Font.BOLD, 28));
		FontMetrics fm = g2.getFontMetrics();
		String title = "CONTROLS";
		int titleWidth = fm.stringWidth(title);
		g2.drawString(title, boxX + (boxWidth - titleWidth) / 2, boxY + 40);

		g2.setColor(new Color(220, 220, 220));
		g2.setFont(new Font("Arial", Font.PLAIN, 14));
		int textX = boxX + 30;
		int textY = boxY + 80;
		int lineHeight = 28;

		String[] lines = new String[] {
				"MOVE: W/A/S/D or Arrow Keys — Move your chef around the kitchen.",
				"INTERACT: V — Pick up or place ingredients, use stations (press near a station).",
				"DASH: SPACE — Perform a short dash to move faster for a brief moment.",
				"THROW: K — Throw the item you hold to another chef or into a station.",
				"\n",
				"Tips:",
				"- Use dash to cross gaps or avoid hazards and save time.",
				"- Interact near an ingredient or station to pick up/place items.",
				"- Throwing can quickly transfer items between chefs when coordinated."
		};

		for (String l : lines) {
			if (l.equals("\n")) {
				textY += lineHeight / 2;
				continue;
			}
			g2.drawString(l, textX, textY);
			textY += lineHeight;
		}

		g2.setColor(new Color(255, 215, 0));
		g2.setFont(new Font("Arial", Font.PLAIN, 12));
		String footer = "Press ENTER or ESC to return";
		int footerWidth = g2.getFontMetrics().stringWidth(footer);
		g2.drawString(footer, boxX + (boxWidth - footerWidth) / 2, boxY + boxHeight - 24);
	}

	public void drawGame(Graphics2D g2) {
		// Draw black background for top margin (order panel area)
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, gp.screenWidth, gp.topMargin);

		// Draw Map Background Image if available
		if (gp.mapBackground != null) {
			g2.drawImage(gp.mapBackground, 0, gp.topMargin, gp.mapWidth, gp.mapHeight, null);
		} else {
			char[][] grid = gp.gameMap.getGrid();
			for (int row = 0; row < gp.gameMap.getRows(); row++) {
				for (int col = 0; col < gp.gameMap.getCols(); col++) {
					int x = col * gp.tileSize;
					int y = row * gp.tileSize + gp.topMargin;
					char tile = grid[row][col];

					if (tile == 'X') {
						g2.setColor(Color.DARK_GRAY);
						g2.fillRect(x, y, gp.tileSize, gp.tileSize);
					} else if (tile == '.' || tile == 'V') {
						if ((row + col) % 2 == 0)
							g2.setColor(gp.pastelOrange);
						else
							g2.setColor(gp.pastelYellow);
						g2.fillRect(x, y, gp.tileSize, gp.tileSize);
					} else {
						g2.setColor(new Color(100, 150, 255));
						g2.fillRect(x, y, gp.tileSize, gp.tileSize);
					}

					if (tile != 'X' && tile != '.' && tile != 'V') {
						g2.setColor(Color.BLACK);
						g2.drawRect(x, y, gp.tileSize, gp.tileSize);
						g2.drawString(String.valueOf(tile), x + 20, y + 30);
					}
				}
			}
		}

		// Draw station markers and progress bars (offset by topMargin)
		for (int row = 0; row < gp.gameMap.getRows(); row++) {
			for (int col = 0; col < gp.gameMap.getCols(); col++) {
				int x = col * gp.tileSize;
				int y = row * gp.tileSize + gp.topMargin; // Offset map down

				nimonscooked.entity.station.Station station = gp.gameMap.getStationAt(col, row);
				if (station instanceof nimonscooked.entity.station.CuttingStation) {
					nimonscooked.entity.station.CuttingStation cuttingStation = (nimonscooked.entity.station.CuttingStation) station;
					if (cuttingStation.getContainedItem() != null &&
							cuttingStation.getSavedTime() > 0) {
						drawProgressBar(g2, x, y, cuttingStation.getSavedTime(),
								cuttingStation.getCuttingDurationMs(), new Color(70, 130, 180));
					}
				} else if (station instanceof nimonscooked.entity.station.CookingStation) {
					nimonscooked.entity.station.CookingStation cookingStation = (nimonscooked.entity.station.CookingStation) station;

					if (cookingStation.getContainedItem() != null) {
						BufferedImage panSprite = cookingStation.getContainedItem().getSprite();
						if (panSprite != null) {
							int panSize = (int) (gp.tileSize * 0.8);
							int panX = x + (gp.tileSize - panSize) / 2;
							int panY = y + (gp.tileSize - panSize) / 2;
							g2.drawImage(panSprite, panX, panY, panSize, panSize, null);
						}

						if (cookingStation.isCooking()) {
							float progress = cookingStation.getCookingProgress();
							Color barColor = progress < 0.8f ? new Color(255, 165, 0) : new Color(255, 69, 0);
							drawProgressBar(g2, x, y, (int) (progress * 100), 100, barColor);
						}
					}
				}
			}
		}

		// Draw Chefs (offset by topMargin)
		List<Chef> chefs = gp.chefs;
		for (int i = 0; i < chefs.size(); i++) {
			Chef c = chefs.get(i);
			int px = c.getVisualX();
			int py = c.getVisualY() + gp.topMargin; // Offset chef position down

			BufferedImage[] sprites = (i == 0) ? gp.chef1Sprites : gp.chef2Sprites;
			BufferedImage imageToDraw = null;

			int baseIndex = 0;
			switch (c.getDirection()) {
				case UP -> baseIndex = 0;
				case DOWN -> baseIndex = 2;
				case LEFT -> baseIndex = 4;
				case RIGHT -> baseIndex = 6;
			}

			if (c.spriteNum == 1)
				imageToDraw = sprites[baseIndex];
			else
				imageToDraw = sprites[baseIndex + 1];

			if (imageToDraw != null) {
				g2.setColor(new Color(0, 0, 0, 70));
				g2.fillOval(px + 8, py + 40, 32, 10);

				g2.drawImage(imageToDraw, px, py, gp.tileSize, gp.tileSize, null);

				// Render held item
				if (c.getInventory() != null) {
					BufferedImage itemImage = c.getInventory().getSprite();
					if (itemImage != null) {
						int itemSize = gp.tileSize / 2;
						int itemX = px + (gp.tileSize - itemSize) / 2;
						int itemY = py - itemSize / 2;

						g2.setColor(new Color(255, 255, 255, 200));
						g2.fillOval(itemX - 2, itemY - 2, itemSize + 4, itemSize + 4);

						g2.drawImage(itemImage, itemX, itemY, itemSize, itemSize, null);
					} else {
						int itemSize = gp.tileSize / 2;
						int itemX = px + (gp.tileSize - itemSize) / 2;
						int itemY = py - itemSize / 2;

						g2.setColor(new Color(255, 255, 255, 200));
						g2.fillRect(itemX - 2, itemY - 2, itemSize + 4, itemSize + 4);
						g2.setColor(new Color(100, 200, 100));
						g2.fillRect(itemX, itemY, itemSize, itemSize);

						g2.setColor(Color.BLACK);
						g2.setFont(new Font("Monospaced", Font.BOLD, 10));
						String itemName = c.getInventory().getName();
						String initial = itemName.length() > 0 ? itemName.substring(0, Math.min(3, itemName.length()))
								: "?";
						g2.drawString(initial, itemX + 4, itemY + 14);
					}
				}

				// Active indicator
				if (i == gp.activeChefIndex && !c.isMoving()) {
					long timeSinceSwitch = System.currentTimeMillis() - gp.lastChefSwitchTime;
					if (timeSinceSwitch < gp.CHEF_INDICATOR_DURATION) {
						g2.setColor(gp.shinyGoldGlow);
						g2.setStroke(new BasicStroke(5));
						g2.drawRect(px - 2, py - 2, gp.tileSize + 4, gp.tileSize + 4);

						g2.setColor(gp.shinyGold);
						g2.setStroke(new BasicStroke(2));
						g2.drawRect(px, py, gp.tileSize, gp.tileSize);
					}
				}
			} else {
				g2.setColor(i == gp.activeChefIndex ? Color.WHITE : Color.GRAY);
				g2.fillRect(px, py, gp.tileSize, gp.tileSize);
			}
		}

		// Draw items on floor
		for (int row = 0; row < gp.gameMap.getRows(); row++) {
			for (int col = 0; col < gp.gameMap.getCols(); col++) {
				if (gp.gameMap.hasItemOnFloor(col, row)) {
					nimonscooked.entity.item.Item floorItem = gp.gameMap.getItemOnFloor(col, row);
					if (floorItem != null) {
						int x = col * gp.tileSize;
						int y = row * gp.tileSize + gp.topMargin;

						if (floorItem.getSprite() != null) {
							g2.drawImage(floorItem.getSprite(), x + 8, y + 8, gp.tileSize - 16, gp.tileSize - 16, null);
						} else {
							g2.setColor(new Color(255, 200, 0));
							g2.fillOval(x + 12, y + 12, gp.tileSize - 24, gp.tileSize - 24);
						}

						g2.setColor(new Color(255, 255, 255, 150));
						g2.setStroke(new BasicStroke(2));
						g2.drawOval(x + 10, y + 10, gp.tileSize - 20, gp.tileSize - 20);
					}
				}
			}
		}

		// UI Overlays
		drawOrders(g2);
		drawUI(g2);

		if (gp.isPaused && gp.gameState == GamePanel.GameState.PLAYING) {
			// Pause handled by UI.drawPauseScreen elsewhere
			// caller should invoke drawPauseScreen when appropriate
		}
	}

	public void drawProgressBar(Graphics2D g2, int x, int y, int current, int max, Color barColor) {
		int barWidth = gp.tileSize - 8;
		int barHeight = 6;
		int barX = x + 4;
		int barY = y + gp.tileSize - 10;

		g2.setColor(new Color(50, 50, 50));
		g2.fillRect(barX, barY, barWidth, barHeight);

		float progress = Math.min(1.0f, (float) current / max);
		int fillWidth = (int) (barWidth * progress);
		g2.setColor(barColor);
		g2.fillRect(barX, barY, fillWidth, barHeight);

		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(1));
		g2.drawRect(barX, barY, barWidth, barHeight);
	}

	public void drawOrders(Graphics2D g2) {
		List<Order> orders = gp.orderManager.getActiveOrders();

		// STAGE TIMER - Top-left corner
		drawStageTimer(g2);

		if (orders.isEmpty())
			return;

		// Order panel - centered at top, leaving space for timer and score
		int orderWidth = 120;
		int orderHeight = 50;
		int orderPanelY = 5; // Top of screen
		int totalOrderWidth = orders.size() * (orderWidth + 5);
		int startX = (gp.screenWidth - totalOrderWidth) / 2; // Centered

		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			int orderX = startX + i * (orderWidth + 5);

			float timeRatio = order.getRemainingTime() / order.getDuration();
			Color indicatorColor;
			if (timeRatio > 0.5f) {
				indicatorColor = new Color(100, 255, 100);
			} else if (timeRatio > 0.25f) {
				indicatorColor = new Color(255, 255, 100);
			} else {
				indicatorColor = new Color(255, 100, 100);
			}

			g2.setColor(new Color(50, 50, 50, 220));
			g2.fillRoundRect(orderX, orderPanelY, orderWidth, orderHeight, 8, 8);

			g2.setColor(indicatorColor);
			g2.setStroke(new BasicStroke(3));
			g2.drawRoundRect(orderX, orderPanelY, orderWidth, orderHeight, 8, 8);

			int textX = orderX + 8;
			int textY = orderPanelY + 15;

			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Monospaced", Font.BOLD, 10));
			g2.drawString("#" + order.getId(), textX, textY);

			g2.setFont(new Font("Monospaced", Font.PLAIN, 8));
			String recipeName = order.getRecipe().getName().replace(" Burger", "");
			g2.drawString(recipeName, textX + 20, textY);

			g2.setFont(new Font("Monospaced", Font.PLAIN, 8));
			g2.setColor(new Color(200, 200, 200));

			StringBuilder ingredients = new StringBuilder();
			for (nimonscooked.entity.order.Recipe.Requirement r : order.getRecipe().getRequirements()) {
				String ingShort = "";
				switch (r.name) {
					case "Bun":
						ingShort = "Bun";
						break;
					case "Meat":
						ingShort = "Meat";
						break;
					case "Cheese":
						ingShort = "Ches";
						break;
					case "Lettuce":
						ingShort = "Lett";
						break;
					case "Tomato":
						ingShort = "Toma";
						break;
				}
				String stateSymbol = "";
				switch (r.state) {
					case RAW:
						stateSymbol = "";
						break;
					case CHOPPED:
						stateSymbol = "*";
						break;
					case COOKING:
						stateSymbol = "~";
						break;
					case COOKED:
						stateSymbol = "+";
						break;
					case BURNED:
						stateSymbol = "X";
						break;
				}
				ingredients.append(ingShort).append(stateSymbol).append("  ");
			}
			g2.drawString(ingredients.toString().trim(), textX, textY + 10);

			g2.setColor(indicatorColor);
			g2.setFont(new Font("Monospaced", Font.BOLD, 8));
			g2.drawString(String.format("%.0fs", order.getRemainingTime()), textX, textY + 18);
		}
	}

	public void drawUI(Graphics2D g2) {
		int uiHeight = 80;
		int uiY = gp.screenHeight - uiHeight;

		// SCORE BOX - Bottom-right corner, above chef panel
		int scoreBoxWidth = 200;
		int scoreBoxHeight = 35;
		int scoreBoxX = gp.screenWidth - 210; // Right corner
		int scoreBoxY = uiY - 40; // Above chef panel

		g2.setColor(new Color(0, 0, 0, 220));
		g2.fillRoundRect(scoreBoxX, scoreBoxY, scoreBoxWidth, scoreBoxHeight, 8, 8);
		g2.setColor(new Color(255, 215, 0));
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(scoreBoxX, scoreBoxY, scoreBoxWidth, scoreBoxHeight, 8, 8);

		g2.setFont(new Font("Monospaced", Font.BOLD, 18));
		g2.setColor(new Color(255, 215, 0));
		String scoreText = "SCORE: " + gp.orderManager.getScore();
		FontMetrics fm = g2.getFontMetrics();
		int scoreTextWidth = fm.stringWidth(scoreText);
		g2.drawString(scoreText, scoreBoxX + (scoreBoxWidth - scoreTextWidth) / 2, scoreBoxY + 23);

		// Chef UI Panel - Bottom of screen
		g2.setColor(new Color(0, 0, 0, 180));
		g2.fillRect(0, uiY, gp.screenWidth, uiHeight);

		g2.setColor(new Color(255, 215, 0));
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(0, uiY, gp.screenWidth, uiHeight);

		int chefUIWidth = gp.screenWidth / gp.chefs.size();
		for (int i = 0; i < gp.chefs.size(); i++) {
			Chef chef = gp.chefs.get(i);
			int uiX = i * chefUIWidth;

			if (i == gp.activeChefIndex) {
				long timeSinceSwitch = System.currentTimeMillis() - gp.lastChefSwitchTime;
				if (timeSinceSwitch < gp.CHEF_INDICATOR_DURATION) {
					g2.setColor(new Color(255, 215, 0, 100));
					g2.fillRect(uiX + 5, uiY + 5, chefUIWidth - 10, uiHeight - 10);
				}
			}

			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Monospaced", Font.BOLD, 14));
			g2.drawString(chef.getName(), uiX + 15, uiY + 25);

			if (chef.getInventory() != null) {
				BufferedImage itemSprite = chef.getInventory().getSprite();
				int itemSize = 40;
				int itemX = uiX + 15;
				int itemY = uiY + 30;

				g2.setColor(new Color(255, 255, 255, 230));
				g2.fillRoundRect(itemX - 2, itemY - 2, itemSize + 4, itemSize + 4, 5, 5);

				if (itemSprite != null) {
					g2.drawImage(itemSprite, itemX, itemY, itemSize, itemSize, null);
				} else {
					g2.setColor(new Color(100, 200, 100));
					g2.fillRect(itemX, itemY, itemSize, itemSize);
				}

				g2.setColor(Color.WHITE);
				g2.setFont(new Font("Monospaced", Font.PLAIN, 12));
				String itemName = chef.getInventory().getName();
				g2.drawString(itemName, itemX + itemSize + 8, itemY + 15);

				if (chef.getInventory() instanceof nimonscooked.entity.item.ingredient.Ingredient) {
					nimonscooked.entity.item.ingredient.Ingredient ing = (nimonscooked.entity.item.ingredient.Ingredient) chef
							.getInventory();
					g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
					g2.setColor(new Color(200, 200, 200));
					g2.drawString(ing.getState().toString(), itemX + itemSize + 8, itemY + 30);
				}
			} else {
				g2.setColor(new Color(150, 150, 150));
				g2.setFont(new Font("Monospaced", Font.PLAIN, 12));
				g2.drawString("Empty hands", uiX + 15, uiY + 50);
			}
		}

		g2.setColor(new Color(200, 200, 200));
		g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
		g2.drawString("WASD: Move | V: Interact | TAB: Switch Chef", 10, gp.screenHeight - 5);
	}

	private void drawStageTimer(Graphics2D g2) {
		// Stage Timer Display - Top-left corner
		int timerBoxWidth = 120;
		int timerBoxHeight = 35;
		int timerBoxX = 5;
		int timerBoxY = 5;

		float timeRemaining = gp.orderManager.getStageTimeRemaining();
		String timeText = gp.orderManager.getFormattedStageTime();

		// Color based on time remaining
		Color timerColor;
		if (timeRemaining > 90) {
			timerColor = new Color(100, 255, 100); // Green
		} else if (timeRemaining > 30) {
			timerColor = new Color(255, 255, 100); // Yellow
		} else {
			timerColor = new Color(255, 100, 100); // Red (urgent)
		}

		// Draw custom background image if available, otherwise default box
		if (gp.timerBackground != null) {
			// Draw custom timer background image
			g2.drawImage(gp.timerBackground, timerBoxX, timerBoxY, timerBoxWidth, timerBoxHeight, null);
		} else {
			// Fallback: Default background box
			g2.setColor(new Color(0, 0, 0, 220));
			g2.fillRoundRect(timerBoxX, timerBoxY, timerBoxWidth, timerBoxHeight, 8, 8);
			g2.setColor(timerColor);
			g2.setStroke(new BasicStroke(3));
			g2.drawRoundRect(timerBoxX, timerBoxY, timerBoxWidth, timerBoxHeight, 8, 8);
		}

		// Timer text (always drawn on top)
		g2.setFont(new Font("Monospaced", Font.BOLD, 18));
		g2.setColor(timerColor);
		FontMetrics fm = g2.getFontMetrics();
		int textWidth = fm.stringWidth(timeText);
		g2.drawString(timeText, timerBoxX + (timerBoxWidth - textWidth) / 2, timerBoxY + 23);
	}

	public void drawGameOverScreen(Graphics2D g2) {
		BufferedImage background = (gp.gameState == GamePanel.GameState.WIN) ? gp.winScreenBackground
				: gp.loseScreenBackground;

		if (background != null) {
			// Draw background image
			g2.drawImage(background, 0, 0, gp.screenWidth, gp.screenHeight, null);

			int buttonWidth = 300;
			int buttonHeight = 60;
			int buttonX = (gp.screenWidth - buttonWidth) / 2;

			int tryAgainY = 400;
			int backY = 480;

			// Draw TRY AGAIN button
			boolean tryAgainSelected = gp.selectedMenuIndex == 0;

			// Button background box
			g2.setColor(tryAgainSelected ? new Color(70, 170, 70, 230) : new Color(50, 150, 50, 200));
			g2.fillRoundRect(buttonX, tryAgainY, buttonWidth, buttonHeight, 15, 15);

			// Button border
			g2.setColor(tryAgainSelected ? new Color(100, 255, 100) : new Color(80, 200, 80));
			g2.setStroke(new BasicStroke(tryAgainSelected ? 5 : 3));
			g2.drawRoundRect(buttonX, tryAgainY, buttonWidth, buttonHeight, 15, 15);

			// Button text
			g2.setFont(new Font("Monospaced", Font.BOLD, 28));
			FontMetrics fm = g2.getFontMetrics();
			g2.setColor(Color.WHITE);
			String tryAgainText = "TRY AGAIN";
			int tryAgainTextWidth = fm.stringWidth(tryAgainText);
			g2.drawString(tryAgainText, buttonX + (buttonWidth - tryAgainTextWidth) / 2, tryAgainY + 38);

			// Draw BACK TO MENU button
			boolean backSelected = gp.selectedMenuIndex == 1;

			// Button background box
			g2.setColor(backSelected ? new Color(120, 120, 120, 230) : new Color(100, 100, 100, 200));
			g2.fillRoundRect(buttonX, backY, buttonWidth, buttonHeight, 15, 15);

			// Button border
			g2.setColor(backSelected ? Color.WHITE : new Color(180, 180, 180));
			g2.setStroke(new BasicStroke(backSelected ? 5 : 3));
			g2.drawRoundRect(buttonX, backY, buttonWidth, buttonHeight, 15, 15);

			// Button text
			g2.setColor(Color.WHITE);
			String backText = "BACK TO MENU";
			int backTextWidth = fm.stringWidth(backText);
			g2.drawString(backText, buttonX + (buttonWidth - backTextWidth) / 2, backY + 38);

		} else {
			g2.setColor(new Color(0, 0, 0, 200));
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

			// Shift everything up by using offset from top instead of center
			int startY = 120; // Start from top instead of center

			g2.setFont(new Font("Monospaced", Font.BOLD, 72));
			String title = (gp.gameState == GamePanel.GameState.WIN) ? "YOU WIN!" : "YOU LOSE!";
			Color titleColor = (gp.gameState == GamePanel.GameState.WIN) ? new Color(255, 215, 0)
					: new Color(255, 50, 50);
			g2.setColor(titleColor);

			FontMetrics fm = g2.getFontMetrics();
			int titleWidth = fm.stringWidth(title);
			g2.drawString(title, (gp.screenWidth - titleWidth) / 2, startY);

			// Final Score
			g2.setFont(new Font("Monospaced", Font.PLAIN, 32));
			String scoreText = "Final Score: " + gp.orderManager.getScore();
			int scoreWidth = g2.getFontMetrics().stringWidth(scoreText);
			g2.setColor(Color.WHITE);
			g2.drawString(scoreText, (gp.screenWidth - scoreWidth) / 2, startY + 80);

			// Time Stats
			g2.setFont(new Font("Monospaced", Font.PLAIN, 24));
			String timeText = "Time: " + gp.orderManager.getFormattedStageTime();
			int timeWidth = g2.getFontMetrics().stringWidth(timeText);
			Color timeColor = gp.orderManager.isStageTimeUp() ? new Color(255, 100, 100) : new Color(100, 255, 100);
			g2.setColor(timeColor);
			g2.drawString(timeText, (gp.screenWidth - timeWidth) / 2, startY + 130);

			// Order Stats
			g2.setFont(new Font("Monospaced", Font.PLAIN, 24));
			String statsText = "Completed: " + gp.orderManager.getCompletedOrders() + " | Expired: "
					+ gp.orderManager.getExpiredOrders();
			int statsWidth = g2.getFontMetrics().stringWidth(statsText);
			g2.setColor(Color.WHITE);
			g2.drawString(statsText, (gp.screenWidth - statsWidth) / 2, startY + 180);

			int buttonWidth = 200;
			int buttonHeight = 60;
			int buttonX = (gp.screenWidth - buttonWidth) / 2;
			int buttonY = startY + 250;
			int backButtonY = buttonY + 70;

			boolean tryAgainSelected = gp.selectedMenuIndex == 0;
			g2.setColor(tryAgainSelected ? new Color(70, 170, 70) : new Color(50, 150, 50));
			g2.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 15, 15);
			g2.setColor(new Color(100, 255, 100));
			g2.setStroke(new BasicStroke(tryAgainSelected ? 5 : 3));
			g2.drawRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 15, 15);

			g2.setFont(new Font("Monospaced", Font.BOLD, 28));
			String buttonText = "Try Again";
			int buttonTextWidth = g2.getFontMetrics().stringWidth(buttonText);
			g2.setColor(Color.WHITE);
			g2.drawString(buttonText, buttonX + (buttonWidth - buttonTextWidth) / 2, buttonY + 38);

			boolean backSelected = gp.selectedMenuIndex == 1;
			g2.setColor(backSelected ? new Color(120, 120, 120) : new Color(100, 100, 100));
			g2.fillRoundRect(buttonX, backButtonY, buttonWidth, buttonHeight, 15, 15);
			g2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(backSelected ? 5 : 3));
			g2.drawRoundRect(buttonX, backButtonY, buttonWidth, buttonHeight, 15, 15);

			String backText = "Back to Menu";
			int backTextWidth = g2.getFontMetrics().stringWidth(backText);
			g2.drawString(backText, buttonX + (buttonWidth - backTextWidth) / 2, backButtonY + 38);
		}

		g2.setFont(new Font("Monospaced", Font.PLAIN, 18));
		g2.setColor(new Color(255, 255, 255));
		String instruction = "W/S to navigate | ENTER/SPACE to select | ESC for menu";
		int instrWidth = g2.getFontMetrics().stringWidth(instruction);
		g2.drawString(instruction, (gp.screenWidth - instrWidth) / 2, gp.screenHeight - 40);
	}

	public void drawMainMenu(Graphics2D g2) {
		// if (gp.menuBackground != null) {
		final Color PRIM_COLOR = new Color(121, 51, 51);
		final Color FILL_COLOR = new Color(251, 237, 198, 220);
		final int TEXT_PADDING = 12;

		g2.drawImage(gp.menuBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);

		int buttonWidth = 150;
		int buttonHeight = 45;
		int gapY = 15;
		int buttonX = 45;

		int totalMenuHeight = (4 * buttonHeight) + (3 * gapY);
		int currentY = 240;

		g2.setFont(new Font("daydream", Font.PLAIN, 18));
		FontMetrics fm = g2.getFontMetrics();

		String[] texts = { "START", "CONTROL", "CREDIT", "EXIT" };

		for (int i = 0; i < texts.length; i++) {
			boolean selected = gp.selectedMenuIndex == i;
			String text = texts[i];
			int rectY = currentY;

			g2.setColor(FILL_COLOR);
			g2.fillRoundRect(buttonX, rectY, buttonWidth, buttonHeight, 15, 15);

			g2.setColor(PRIM_COLOR);
			g2.setStroke(new BasicStroke(selected ? 6 : 3));
			g2.drawRoundRect(buttonX, rectY, buttonWidth, buttonHeight, 15, 15);
			g2.setColor(PRIM_COLOR);

			int textX = buttonX + TEXT_PADDING;
			int textY = currentY + 30;

			g2.drawString(text, textX, textY);

			if (selected) {
				g2.fillPolygon(new int[] { buttonX - 20, buttonX - 10, buttonX - 20 },
						new int[] { textY - 10, textY, textY + 10 }, 3);
			}
			currentY += buttonHeight + gapY;
		}

		// } else {
		// g2.setColor(new Color(255, 228, 196));
		// g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

		// g2.setFont(new Font("Monospaced", Font.BOLD, 80));
		// g2.setColor(new Color(255, 100, 50));
		// String title = "NIMONSCOOKED";
		// int titleWidth = g2.getFontMetrics().stringWidth(title);
		// g2.drawString(title, (gp.screenWidth - titleWidth) / 2, 150);

		// g2.setFont(new Font("Monospaced", Font.PLAIN, 24));
		// g2.setColor(new Color(100, 100, 100));
		// String subtitle = "Kelompok N - OOP Project";
		// int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
		// g2.drawString(subtitle, (gp.screenWidth - subtitleWidth) / 2, 190);

		// int buttonWidth = 300;
		// int buttonHeight = 60;
		// int buttonX = (gp.screenWidth - buttonWidth) / 2;
		// int startY = 280;
		// int spacing = 80;

		// boolean startSelected = gp.selectedMenuIndex == 0;
		// g2.setColor(startSelected ? new Color(70, 225, 70) : new Color(50, 205, 50));
		// g2.fillRoundRect(buttonX, startY, buttonWidth, buttonHeight, 20, 20);
		// g2.setColor(Color.WHITE);
		// g2.setStroke(new BasicStroke(startSelected ? 5 : 3));
		// g2.drawRoundRect(buttonX, startY, buttonWidth, buttonHeight, 20, 20);
		// g2.setFont(new Font("Arial", Font.BOLD, 32));
		// String startText = "Start Game";
		// int startTextWidth = g2.getFontMetrics().stringWidth(startText);
		// g2.drawString(startText, buttonX + (buttonWidth - startTextWidth) / 2, startY
		// + 40);

		// int howToPlayY = startY + spacing;
		// boolean howToPlaySelected = gp.selectedMenuIndex == 1;
		// g2.setColor(howToPlaySelected ? new Color(50, 164, 255) : new Color(30, 144,
		// 255));
		// g2.fillRoundRect(buttonX, howToPlayY, buttonWidth, buttonHeight, 20, 20);
		// g2.setColor(Color.WHITE);
		// g2.setStroke(new BasicStroke(howToPlaySelected ? 5 : 3));
		// g2.drawRoundRect(buttonX, howToPlayY, buttonWidth, buttonHeight, 20, 20);
		// String howToPlayText = "How to Play";
		// int howToPlayTextWidth = g2.getFontMetrics().stringWidth(howToPlayText);
		// g2.drawString(howToPlayText, buttonX + (buttonWidth - howToPlayTextWidth) /
		// 2, howToPlayY + 40);

		// int exitY = howToPlayY + spacing;
		// boolean exitSelected = gp.selectedMenuIndex == 2;
		// g2.setColor(exitSelected ? new Color(240, 40, 80) : new Color(220, 20, 60));
		// g2.fillRoundRect(buttonX, exitY, buttonWidth, buttonHeight, 20, 20);
		// g2.setColor(Color.WHITE);
		// g2.setStroke(new BasicStroke(exitSelected ? 5 : 3));
		// g2.drawRoundRect(buttonX, exitY, buttonWidth, buttonHeight, 20, 20);
		// String exitText = "Exit";
		// int exitTextWidth = g2.getFontMetrics().stringWidth(exitText);
		// g2.drawString(exitText, buttonX + (buttonWidth - exitTextWidth) / 2, exitY +
		// 40);
		// }

		// g2.setColor(Color.WHITE);
		// g2.setFont(new Font("Arial", Font.PLAIN, 18));
		// String hint = "Use W/S or Arrow Keys to navigate | ENTER/SPACE to select";
		// int hintWidth = g2.getFontMetrics().stringWidth(hint);
		// g2.drawString(hint, (gp.screenWidth - hintWidth) / 2, gp.screenHeight - 30);
	}

	public void drawHowToPlay(Graphics2D g2) {
		g2.setColor(new Color(240, 248, 255));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

		g2.setFont(new Font("Arial", Font.BOLD, 48));
		g2.setColor(new Color(30, 144, 255));
		String title = "How to Play";
		int titleWidth = g2.getFontMetrics().stringWidth(title);
		g2.drawString(title, (gp.screenWidth - titleWidth) / 2, 80);

		g2.setFont(new Font("Arial", Font.PLAIN, 20));
		g2.setColor(Color.BLACK);
		int startY = 150;
		int lineHeight = 35;

		String[] instructions = {
				"Controls:",
				"  WASD - Move your chef",
				"  V - Interact (pick/place/use items)",
				"  C - Drop item",
				"  B - Switch between chefs",
				"  F - Serve order from plate",
				"  X - Trash item",
				"",
				"Objective:",
				"  • Prepare orders correctly and quickly",
				"  • Complete 1 order to WIN",
				"  • Score drops to -10: GAME OVER",
				"",
				"Tips:",
				"  • Raw ingredients need preparation",
				"  • Watch cooking times to avoid burning",
				"  • Complete orders before timer expires"
		};

		for (int i = 0; i < instructions.length; i++) {
			g2.drawString(instructions[i], 100, startY + i * lineHeight);
		}

		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Arial", Font.PLAIN, 18));
		String hint = "Press ENTER, SPACE, or ESC to return to menu";
		int hintWidth = g2.getFontMetrics().stringWidth(hint);
		g2.drawString(hint, (gp.screenWidth - hintWidth) / 2, gp.screenHeight - 30);
	}

	public void drawStageSelect(Graphics2D g2) {
		g2.setColor(new Color(255, 250, 205));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

		g2.setFont(new Font("Arial", Font.BOLD, 56));
		g2.setColor(new Color(255, 140, 0));
		String title = "Select Stage";
		int titleWidth = g2.getFontMetrics().stringWidth(title);
		g2.drawString(title, (gp.screenWidth - titleWidth) / 2, 100);

		g2.setFont(new Font("Arial", Font.PLAIN, 24));
		g2.setColor(Color.BLACK);
		String[] info = {
				"Map: C - Burger Kitchen",
				"Win: Complete 1 order",
				"Lose: Score drops to -10",
				"Recipes: Classic, Cheeseburger, BLT, Deluxe"
		};

		int infoY = 200;
		for (int i = 0; i < info.length; i++) {
			int infoWidth = g2.getFontMetrics().stringWidth(info[i]);
			g2.drawString(info[i], (gp.screenWidth - infoWidth) / 2, infoY + i * 40);
		}

		int buttonWidth = 250;
		int buttonHeight = 60;
		int buttonX = (gp.screenWidth - buttonWidth) / 2;
		int selectY = 380;
		int backY = selectY + 80;

		boolean startSelected = gp.selectedMenuIndex == 0;
		g2.setColor(startSelected ? new Color(70, 225, 70) : new Color(50, 205, 50));
		g2.fillRoundRect(buttonX, selectY, buttonWidth, buttonHeight, 20, 20);
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(startSelected ? 5 : 3));
		g2.drawRoundRect(buttonX, selectY, buttonWidth, buttonHeight, 20, 20);

		g2.setFont(new Font("Arial", Font.BOLD, 32));
		String selectText = "START!";
		int selectTextWidth = g2.getFontMetrics().stringWidth(selectText);
		g2.drawString(selectText, buttonX + (buttonWidth - selectTextWidth) / 2, selectY + 40);

		boolean backSelected = gp.selectedMenuIndex == 1;
		g2.setColor(backSelected ? new Color(120, 120, 120) : new Color(100, 100, 100));
		g2.fillRoundRect(buttonX, backY, buttonWidth, buttonHeight, 20, 20);
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(backSelected ? 5 : 3));
		g2.drawRoundRect(buttonX, backY, buttonWidth, buttonHeight, 20, 20);

		g2.setFont(new Font("Arial", Font.BOLD, 28));
		String backText = "Back";
		int backTextWidth = g2.getFontMetrics().stringWidth(backText);
		g2.drawString(backText, buttonX + (buttonWidth - backTextWidth) / 2, backY + 38);

		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Arial", Font.PLAIN, 18));
		String hint = "Use W/S to navigate | ENTER/SPACE to select | ESC to go back";
		int hintWidth = g2.getFontMetrics().stringWidth(hint);
		g2.drawString(hint, (gp.screenWidth - hintWidth) / 2, gp.screenHeight - 30);
	}
}
