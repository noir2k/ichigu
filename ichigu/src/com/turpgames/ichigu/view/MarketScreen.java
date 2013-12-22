package com.turpgames.ichigu.view;

import com.turpgames.framework.v0.util.Game;
import com.turpgames.ichigu.model.display.IchiguToolbar;
import com.turpgames.ichigu.model.game.IchiguMarket;

public class MarketScreen extends IchiguScreen {
	private IchiguMarket market;

	@Override
	public void init() {
		super.init();
		market = new IchiguMarket();
		registerDrawable(market, Game.LAYER_SCREEN);
	}

	@Override
	protected void onAfterActivate() {
		IchiguToolbar.getInstance().activateBackButton();
		market.activate();
	}

	@Override
	protected boolean onBeforeDeactivate() {
		IchiguToolbar.getInstance().deactivateBackButton();
		market.deactivate();
		return true;
	}
}
