package com.turpgames.ichigu.controller.sudoku;

import com.turpgames.ichigu.utils.Ichigu;

public class SudokuEndState extends SudokuState {
	public SudokuEndState(SudokuController controller) {
		super(controller);
	}

	@Override
	protected void activated() {
		Ichigu.playSoundTimeUp();
		model.endMode();
	}

	@Override
	public void onNewGame() {
		model.startMode();
		controller.setDealingState();
	}
	
	@Override
	public void draw() {
		model.drawResult();
	}
}