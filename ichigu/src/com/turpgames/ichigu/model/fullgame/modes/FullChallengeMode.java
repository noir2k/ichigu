package com.turpgames.ichigu.model.fullgame.modes;

import com.turpgames.framework.v0.impl.Settings;
import com.turpgames.framework.v0.impl.Text;
import com.turpgames.framework.v0.util.CountDownTimer;
import com.turpgames.framework.v0.util.Timer;
import com.turpgames.ichigu.model.display.FoundInfo;
import com.turpgames.ichigu.model.fullgame.FullGameMode;
import com.turpgames.ichigu.utils.Ichigu;
import com.turpgames.ichigu.utils.R;

public class FullChallengeMode extends FullGameMode {
	private static int challengeTime = 5 * 60;

	private FoundInfo foundInfo;
	private CountDownTimer timer;

	public FullChallengeMode() {
		foundInfo = new FoundInfo();
		
//		foundInfo.setAlignment(Text.HAlignCenter, Text.VAlignBottom);
//		foundInfo.setPadding(0, 55);
		
		foundInfo.setAlignment(Text.HAlignLeft, Text.VAlignTop);
		foundInfo.setPadding(10, 110);
	}

	@Override
	protected Timer getTimer() {
		if (timer == null) {
			timer = new CountDownTimer(challengeTime);
			timer.setInterval(0.5f);
			timer.setCountDownListener(new CountDownTimer.ICountDownListener() {
				@Override
				public void onCountDownEnd(CountDownTimer timer) {
					notifyModeEnd();
				}
			});
		}
		return timer;
	}

	@Override
	protected void onStartMode() {
		super.onStartMode();
		foundInfo.reset();
	}
	
	@Override
	protected void prepareResultInfoAndSaveHiscore() {
		int hiScore = Settings.getInteger(R.settings.hiscores.fullchallenge, 0);
		int score = foundInfo.getFound();
		if (score > hiScore)
			Settings.putInteger(R.settings.hiscores.fullchallenge, score);

		resultInfo.setText(String.format(Ichigu.getString(R.strings.fullChallengeResult),
				score, (score > hiScore ? Ichigu.getString(R.strings.newHiscore) : "")));
	}
	
	@Override
	protected void onDraw() {
		foundInfo.draw();
		super.onDraw();
	}

	@Override
	public void concreteIchiguFound() {
		foundInfo.increaseFound();
		super.concreteIchiguFound();
	}
}