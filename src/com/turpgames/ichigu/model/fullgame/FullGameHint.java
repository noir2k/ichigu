package com.turpgames.ichigu.model.fullgame;

import com.turpgames.framework.v0.IDrawable;
import com.turpgames.framework.v0.component.BlinkingImageButton;
import com.turpgames.framework.v0.component.IButtonListener;
import com.turpgames.framework.v0.effects.IEffectEndListener;
import com.turpgames.framework.v0.forms.xml.Toast;
import com.turpgames.framework.v0.impl.Text;
import com.turpgames.framework.v0.util.Game;
import com.turpgames.framework.v0.util.Timer;
import com.turpgames.framework.v0.util.Vector;
import com.turpgames.ichigu.model.game.Card;
import com.turpgames.ichigu.model.game.IchiguBank;
import com.turpgames.ichigu.utils.Ichigu;
import com.turpgames.ichigu.utils.R;

public class FullGameHint implements IDrawable, IEffectEndListener, Toast.IToastListener {
	private final static int notificationInterval = 30;
	private static final float buttonSize = Game.scale(R.ui.imageButtonWidth);

	private BlinkingImageButton button;
	private Text hintCountText;
	private FullGameIchiguInfo ichiguInfo;
	private Card[] cards;
	private String text;
	private int hintIndex;
	private boolean isActive;
	private Timer notificationTimer;
	private int prevIchiguCount = 0;

	private Toast toast;

	private IHintListener hintListener;

	FullGameHint() {
		button = new BlinkingImageButton(buttonSize, buttonSize, R.colors.ichiguWhite, R.colors.ichiguCyan, 1f, 10);
		button.setTexture(R.game.textures.hint);
		button.setListener(new IButtonListener() {
			@Override
			public void onButtonTapped() {
				onHintButtonTapped();
			}
		});
		
		hintCountText = new Text();
		hintCountText.setText("(" + IchiguBank.getHintCount() + ")");

		ichiguInfo = new FullGameIchiguInfo();

		notificationTimer = new Timer();
		notificationTimer.setInterval(notificationInterval);
		notificationTimer.setTickListener(new Timer.ITimerTickListener() {
			@Override
			public void timerTick(Timer timer) {
				button.blink();
			}
		});

		toast = new Toast();
		toast.setListener(this);
		toast.setToastColor(R.colors.ichiguYellow);
	}

	private void onHintButtonTapped() {
		if (IchiguBank.hasHint()) {
			IchiguBank.decreaseHintCount();

			showNextHint();
			restartNotificationTimer();
		}
		else {
			hintListener.onInsufficientHint();
		}
	}

	public Vector getLocation() {
		return button.getLocation();
	}

	public float getWidth() {
		return button.getWidth();
	}

	public float getHeight() {
		return button.getHeight();
	}

	public void restartNotificationTimer() {
		notificationTimer.restart();
	}

	public void update(Card[] cards) {
		ichiguInfo.update(cards);
		updateText();
		toast.hide();
		hintIndex = 0;
		isActive = false;
		this.cards = cards;
	}

	public int getIchiguCount() {
		return ichiguInfo.getIchiguCount();
	}

	public void setHintListener(IHintListener hintListener) {
		this.hintListener = hintListener;
	}

	private void showNextHint() {
		if (isActive) {
			toast.hide();
			return;
		}

		if (hintIndex == 0) {
			toast.show(text, 3f);
			if (prevIchiguCount != ichiguInfo.getIchiguCount())
				toast.setText(text);
		}
		else {
			int cardIndex = ichiguInfo.getIchiguCardIndex(hintIndex - 1, 1);
			cards[cardIndex].blink(this, false);
		}

		isActive = true;

		prevIchiguCount = ichiguInfo.getIchiguCount();
	}

	private void updateText() {
		int count = ichiguInfo.getIchiguCount();
		if (count < 1)
			text = Ichigu.getString(R.strings.noIchigu);
		else if (count == 1)
			text = Ichigu.getString(R.strings.oneIchigu);
		else
			text = count + " " + Ichigu.getString(R.strings.someIchigu);
	}

	private void hintEnd() {
		hintIndex = (hintIndex + 1) % (1 + ichiguInfo.getIchiguCount());
		isActive = false;
	}

	@Override
	public boolean onEffectEnd(Object card) {
		hintEnd();
		return true;
	}

	@Override
	public void onToastHidden(Toast toast) {
		hintEnd();
	}

	@Override
	public void draw() {
		drawButton();
	}

	private void drawButton() {
		button.draw();
	}

	public void activate() {
		restartNotificationTimer();
		button.listenInput(true);
	}

	public void deactivate() {
		button.listenInput(false);
		toast.dispose();
		notificationTimer.stop();
	}

	public boolean isActive() {
		return isActive;
	}
}