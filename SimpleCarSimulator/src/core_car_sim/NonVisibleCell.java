package core_car_sim;

import java.io.Serial;

public class NonVisibleCell extends AbstractCell{
	
	@Serial
	private static final long serialVersionUID = -5538481572551247336L;
	public NonVisibleCell(){
		super(CellType.ct_non_visible);
	}
	@Override
	public void stepSim(){}

	@Override
	public boolean isDriveable(){
		return false;
	}

}
