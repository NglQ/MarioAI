package competition.rl_ai.angeloquarta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import competition.rl_ai.angeloquarta.enums.Distance;
import competition.rl_ai.angeloquarta.enums.ExtendedActions;
import competition.rl_ai.angeloquarta.enums.MarioMode;
import competition.rl_ai.angeloquarta.enums.PositionX;
import competition.rl_ai.angeloquarta.enums.PositionY;
import competition.rl_ai.angeloquarta.enums.Size;
import competition.rl_ai.angeloquarta.enums.Type;


public class NewAgent extends BasicMarioAIAgent implements Agent{

	private double[][] qTable;
	private double[][] rTable;
	private static float ALPHA = 0.5f;
	private static float GAMMA = 0.3f;
	private static double EPSILON=0.45;
	private static final int VIEW_SIZE = 7;
	
	private ExtendedActions prevAction;
	private ExtendedActions currentAction;
	private State prevState;
	private State currentState;
	
	private HashMap<State,Integer> stateMap;
	private HashMap<Integer,State> indexMap;
	
	public NewAgent() {
		super("ForwardJumpingAgent");
		reset();
		
		generateStates();
		initQtable();
		initRtable();
		
		prevState = new State(new EnemyState(Distance.FAR, PositionX.NONE, PositionY.NONE, Type.NONE),new ObstacleState(Distance.FAR, PositionX.NONE, PositionY.NONE, Size.NONE),MarioMode.FIRE);
		prevAction = ExtendedActions.RIGHT;
	}
	
	private int stateTranslation(State state) {
		return stateMap.get(state).intValue();
	}
	
	private void initQtable() {
		Random rand = new Random();
		qTable = new double[stateMap.size()][ExtendedActions.values().length];
		for(int i = 0; i < qTable.length; i++) {
			for(int j = 0; j < qTable[i].length; j++) {
				qTable[i][j] = rand.nextDouble();
			}
		}
	}
	
	private ArrayList<EnemyState> generateEnemyStates(){
		
		ArrayList<EnemyState> enemyStates = new ArrayList<EnemyState>();
		Distance[] distValues = Distance.values();
		PositionX[] posXValues = PositionX.values();
		PositionY[] posYValues = PositionY.values();
		Type[] typeValues = Type.values();
		
		//EnemyState
		for(int distListEnemy = 0; distListEnemy<distValues.length; distListEnemy++) { 
			
			if(distValues[distListEnemy] == Distance.FAR) {
				
				PositionX xPos = PositionX.NONE;
				PositionY yPos = PositionY.NONE;
				Type enemyType = Type.NONE;
				
				EnemyState es = new EnemyState(distValues[distListEnemy], xPos, yPos, enemyType);
				enemyStates.add(es);
				
			}else {
				
				//Horizontal Position
				for (int horizontalList = 0; horizontalList < posXValues.length - 1; horizontalList++) {
					PositionX xPos = posXValues[horizontalList];
					
					//VerticalPosition
					for (int verticalList = 0; verticalList < posYValues.length - 1; verticalList++) {
						PositionY yPos = posYValues[verticalList];
						
						//EnemyKind
						for (int enemyList = 0; enemyList < typeValues.length - 1; enemyList++ ) {
							Type enemyType = typeValues[enemyList];
							
							EnemyState es = new EnemyState(distValues[distListEnemy], xPos, yPos, enemyType);
							enemyStates.add(es);
						}
						
					}
					
				}
				
			}
			
		}
		
		return enemyStates;
	}
	
	private ArrayList<ObstacleState> generateObstacleStates(){
		
		ArrayList<ObstacleState> obstacleStates = new ArrayList<ObstacleState>();
		Distance[] distValues = Distance.values();
		PositionX[] posXValues = PositionX.values();
		PositionY[] posYValues = PositionY.values();
		Size[] sizeValues = Size.values();
		
		//ObstacleState
		for(int distListObstacle = 0; distListObstacle<distValues.length; distListObstacle++) { 
			
			if(distValues[distListObstacle] == Distance.FAR) {
				
				PositionX xPos = PositionX.NONE;
				PositionY yPos = PositionY.NONE;
				Size obstacleSize = Size.NONE;
				
				ObstacleState os = new ObstacleState(distValues[distListObstacle], xPos, yPos, obstacleSize);
				obstacleStates.add(os);
				
			}else {
				
				//Horizontal Position
				for (int horizontalList = 0; horizontalList < posXValues.length - 1; horizontalList++) {
					PositionX xPos = posXValues[horizontalList];
					
					//VerticalPosition
					for (int verticalList = 0; verticalList < posYValues.length - 1; verticalList++) {
						PositionY yPos = posYValues[verticalList];
						
						//EnemyKind
						for (int sizeList = 0; sizeList < sizeValues.length - 1; sizeList++ ) {
							Size enemyType = sizeValues[sizeList];
							
							ObstacleState os = new ObstacleState(distValues[distListObstacle], xPos, yPos, enemyType);
							obstacleStates.add(os);
						}
						
					}
					
				}
				
			}
			
		}
		
		return obstacleStates;
	}
	
	private void generateStates() {
		
		int counter = 0;
		stateMap = new HashMap<State, Integer>();
		indexMap = new HashMap<Integer, State>();
		ArrayList<EnemyState> enemyStates = generateEnemyStates();
		ArrayList<ObstacleState> obstacleStates = generateObstacleStates();
		MarioMode[] marioValues = MarioMode.values();
		
		for (int i = 0; i < enemyStates.size(); i++) {
			
			for(int j = 0; j < obstacleStates.size(); j++) {
				
				for (int k = 0; k < marioValues.length; k++) {
					State s = new State(enemyStates.get(i),obstacleStates.get(j), marioValues[k]);
					
					stateMap.put(s, Integer.valueOf(counter));
					indexMap.put(Integer.valueOf(counter), s);
					counter++;
				}
				
			}
			
		}
		
	}
	
	private void initRtable() {
		rTable = new double[stateMap.size()][ExtendedActions.values().length];
		for(int i = 0; i < rTable.length; i++) {
			for(int j = 0; j < rTable[i].length; j++) {
				rTable[i][j] = 0.0;
			}
		}
		
		for(int i = 0; i < rTable.length; i++) {
			for(int j = 0; j < rTable[i].length; j++) {
				rTable[i][j] = getReward(i,j);
			}
		}
		
	}
	
	private double getReward(int i, int j) {
		
		double reward = rTable[i][j];
		
		ExtendedActions action = ExtendedActions.values()[j];
		State state = indexMap.get(i);
		
		Distance enemyDistance = state.getEnemyState().getEnemyDistance();
		PositionX enemyHoriziontalPosition = state.getEnemyState().getEnemyHorizontalPosition();
		PositionY enemyVerticalPosition = state.getEnemyState().getEnemyVerticalPosition();
		Type enemyKind = state.getEnemyState().getEnemyKind();
		
		Distance obstacleDistance = state.getObstacleState().getObstacleDistance();
		PositionX obstacleHoriziontalPosition = state.getObstacleState().getObstacleHorizontalPosition();
		PositionY obstacleVerticalPosition = state.getObstacleState().getObstacleVerticalPosition();
		Size obstacleSize = state.getObstacleState().getObstacleSize();
		
		MarioMode marioMode = state.getMarioMode();
		
		boolean stateCondition=(enemyDistance == Distance.NEAR || obstacleDistance == Distance.NEAR) 
				&& (enemyHoriziontalPosition == PositionX.FRONT || obstacleHoriziontalPosition == PositionX.FRONT ) 
				&& !(enemyVerticalPosition == PositionY.HIGH || enemyVerticalPosition == PositionY.HIGH);
		
		boolean actionCondition=(action==ExtendedActions.JUMP) || (action==ExtendedActions.LEFT_JUMP) 
				|| (action==ExtendedActions.RIGHT_JUMP) || (action==ExtendedActions.RIGHT_SPEED_JUMP) 
				|| (action==ExtendedActions.LEFT_SPEED_JUMP);
		
		if(stateCondition && actionCondition) {
			reward++;
		}
		
		actionCondition=(action==ExtendedActions.RIGHT) || (action==ExtendedActions.RIGHT_JUMP) 
				|| (action==ExtendedActions.RIGHT_SPEED_JUMP) || (action==ExtendedActions.RIGHT_SPEED);
		
		if(actionCondition) {
			reward++;
		}
		
		return reward;
	}
	
	//TODO: immediate reward will take account of killed monsters and taken coins
	private double getImmediateReward() {
		double immediateReward = 0.0;
		return immediateReward;
	}
	
	
	public boolean[] getAction() {
		for (int i = 0; i < action.length;  i++) 
		{
			action[i] = false;
		}
		
		currentState = getCurrentState();
		currentAction = getCurrentAction();
		updateQtable();
		prevState = currentState;
		prevAction = currentAction;
		
		updateEpsilon();
		
		return actionTranslation(currentAction);
	}
	
	private Distance getDiscerteDistance(int posx, int egoPosx) {
		
		Distance dist;
		int abs = Math.abs(posx - egoPosx);

		if (abs <= State.X_POSITION_LIMIT) {
			dist = Distance.NEAR;
		} else {
			dist = Distance.FAR;
		}

		return dist;
		
	}
	
	private PositionX getDiscretePosX(boolean isFar, int pos, int egoPosx) {

		PositionX posx;
		if (isFar) {
			posx = PositionX.NONE;
		}
		else if (egoPosx > pos) {
			posx = PositionX.BACK;
		} else {
			posx = PositionX.FRONT;
		}

		return posx;
	}
	
	private PositionY getDiscretePosY(boolean isFar, int pos, int egoPosy) {
		PositionY posy;
		
		if (isFar) {
			posy = PositionY.NONE;
		}
		else if(pos > egoPosy + State.Y_HIGH_POSITION_LIMIT){
			posy = PositionY.HIGH;
		}else if(pos > egoPosy - State.Y_LOW_POSITION_LIMIT){
			posy = PositionY.MIDDLE;
		}else{
			posy = PositionY.LOW;
		}

		return posy;
	}
	
	private Type getDiscreteType(boolean isFar,int enemy) {
		
		Type type;
		if(isFar || enemy == 0) {
			type = Type.NONE;
		}else if((enemy  >= 80 && enemy  <= 82) || (enemy  >= 95 &&  enemy  <= 98)){
			type = Type.STOMP;
		}else {
			type = Type.FIRE;
		}
		
		return type;
	}
	
	private Size getDiscreteSize(boolean isFar, int height) {
		Size size;
		
		if(isFar) {
			size = Size.NONE;
		}else if(height > State.BIG_LIMIT_OBSTACLE) {
			size = Size.BIG;
		}else if(height > State.SMALL_LIMIT_OBSTACLE){
			size = Size.MEDIUM;
		}else {
			size = Size.SMALL;
		}
		
		return size;
	}
	
	//TODO: Check the correctness of the current states generation (Apparently it doesn't show the pipes in the correct position)  
	private State getCurrentState() {
		
		MarioEnvironment e = MarioEnvironment.getInstance();
		byte[][] enemies = e.getEnemiesObservationZ(0);
		byte[][] obstacles = e.getLevelSceneObservationZ(0);
				
		int[] egoPos = e.getMarioEgoPos();
		int rfw = e.getReceptiveFieldWidth();
		int rfh = e.getReceptiveFieldHeight();
		//System.out.println("receptiveField: " + rfw + " " +rfh);
		//System.out.println("egopos: " + egoPos[0] + " " +egoPos[1]);
		
		int[] posEn = new int[2];
		posEn[0] = enemies.length;
		posEn[1] = enemies[0].length;
		int enemyKindInt = 0;
		
		int[] posOb = new int[2];
		posOb[0] = obstacles.length;
		posOb[1] = obstacles[0].length;
		int obstacleSizeInt = 0;
		
		double enemyDistanceDouble = Double.MAX_VALUE;
		double obstacleDistanceDouble = Double.MAX_VALUE;
		
		for (int i = (egoPos[1] - VIEW_SIZE); i < ((egoPos[1] + VIEW_SIZE)); i++) {
			
			String s=new String("");
			
			for (int j = (egoPos[0] - VIEW_SIZE); j < (egoPos[0] + VIEW_SIZE); j++) {
				
				if (enemies[i][j] != 0 && enemies[i][j] != 25) {
					double distance =  Math.sqrt((i - egoPos[1]) ^ 2 + (j - egoPos[0]) ^ 2);
					if (distance <= enemyDistanceDouble) {
						posEn[1] = i;
						posEn[0] = j;
						enemyKindInt = enemies[i][j];
						enemyDistanceDouble=distance;
					}
				}
				
				if (obstacles[i][j] != 0) {
					

					int lookUpStart = egoPos[1];
					int lookUpEnd = egoPos[1] + VIEW_SIZE;
					int idx = lookUpStart;
					int val = obstacles[idx][j]; 
					while (val != 0 && idx < lookUpEnd) {
						val = obstacles[idx][j];
						obstacleSizeInt ++;
						idx ++;
					}
					
					if(obstacles[i][j] != -60 || (obstacles[i][j] == -60 && obstacleSizeInt >1)) {
						
						double distance =  Math.sqrt((i - egoPos[1]) ^ 2 + (j - egoPos[0]) ^ 2);
						if (distance <= obstacleDistanceDouble) {
							posOb[1] = i;
							posOb[0] = j;
							obstacleDistanceDouble=distance;
						}
						
					}
					
				}
				
			}
			
		}
		
		Distance enemyDistance = getDiscerteDistance(posEn[1], egoPos[1]);
		boolean isEnemyFar = (enemyDistance == Distance.FAR);
		PositionX enemyX = getDiscretePosX(isEnemyFar, posEn[1], egoPos[1]);
		PositionY enemyY = getDiscretePosY(isEnemyFar, posEn[0], egoPos[0]);
		Type enemyKind = getDiscreteType(isEnemyFar, enemyKindInt);
		
		Distance obstacleDistance = getDiscerteDistance(posOb[1], egoPos[1]);
		boolean isObstacleFar = (obstacleDistance == Distance.FAR);
		PositionX obstacleX = getDiscretePosX(isObstacleFar, posOb[1], egoPos[1]);
		PositionY obstacleY = getDiscretePosY(isObstacleFar, posOb[0], egoPos[0]);
		Size obstacleSize = getDiscreteSize(isObstacleFar, obstacleSizeInt);
		
		EnemyState es = new EnemyState(enemyDistance, enemyX, enemyY, enemyKind);
		ObstacleState os = new ObstacleState(obstacleDistance, obstacleX, obstacleY, obstacleSize);
		MarioMode mm = MarioMode.values()[e.getMarioMode()];
		
		State state = new State(es, os, mm); 
		
		return state;
	}
	
	private int max(double[] actions) {
		int result=0;
		
		double maximum = Double.MIN_VALUE;

		for (int i = 0; i < actions.length; i++) {

			if (actions[i] > maximum) {
				maximum = actions[i];
				result = i;
			}

		}
		return result;
	}
	
	private void updateEpsilon() {
		Environment e = MarioEnvironment.getInstance();
		int timeSpent = e.getTimeSpent();
		if(EPSILON > 0.1) {
			EPSILON = EPSILON - 0.0001*timeSpent;
		}  
	}
	
	private ExtendedActions getCurrentAction() {
		
		int result=0;
				
		int currentStateIdx = stateTranslation(currentState);
		double[] actions = qTable[currentStateIdx];
		
		Random rand=new Random();
		if(rand.nextDouble()<EPSILON){
			result=rand.nextInt(ExtendedActions.values().length);
		}else{
			result = max(actions);
		}
		
		return ExtendedActions.values()[result];
	}
	
	private boolean[] actionTranslation(ExtendedActions ea) {
		
		if (ea == ExtendedActions.LEFT || ea == ExtendedActions.RIGHT || ea == ExtendedActions.DOWN ||
			ea == ExtendedActions.JUMP || ea == ExtendedActions.SPEED || ea == ExtendedActions.UP) 
		{
			action[ea.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.LEFT_JUMP) 
		{
			action[ExtendedActions.LEFT.ordinal()] = true;
			action[ExtendedActions.JUMP.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.RIGHT_JUMP) 
		{
			action[ExtendedActions.RIGHT.ordinal()] = true;
			action[ExtendedActions.JUMP.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.DOWN_JUMP) 
		{
			action[ExtendedActions.DOWN.ordinal()] = true;
			action[ExtendedActions.JUMP.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.LEFT_SPEED) 
		{
			action[ExtendedActions.LEFT.ordinal()] = true;
			action[ExtendedActions.SPEED.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.RIGHT_SPEED) 
		{
			action[ExtendedActions.RIGHT.ordinal()] = true;
			action[ExtendedActions.SPEED.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.DOWN_SPEED) 
		{
			action[ExtendedActions.DOWN.ordinal()] = true;
			action[ExtendedActions.SPEED.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.LEFT_SPEED) 
		{
			action[ExtendedActions.LEFT.ordinal()] = true;
			action[ExtendedActions.SPEED.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.LEFT_SPEED) 
		{
			action[ExtendedActions.LEFT.ordinal()] = true;
			action[ExtendedActions.SPEED.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.RIGHT_SPEED) 
		{
			action[ExtendedActions.RIGHT.ordinal()] = true;
			action[ExtendedActions.SPEED.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.DOWN_SPEED) 
		{
			action[ExtendedActions.DOWN.ordinal()] = true;
			action[ExtendedActions.SPEED.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.JUMP_SPEED) 
		{
			action[ExtendedActions.JUMP.ordinal()] = true;
			action[ExtendedActions.SPEED.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.LEFT_SPEED_JUMP) 
		{
			action[ExtendedActions.LEFT.ordinal()] = true;
			action[ExtendedActions.JUMP.ordinal()] = true;
			action[ExtendedActions.SPEED.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.RIGHT_SPEED_JUMP) 
		{
			action[ExtendedActions.RIGHT.ordinal()] = true;
			action[ExtendedActions.JUMP.ordinal()] = true;
			action[ExtendedActions.SPEED.ordinal()] = true;
		}
		
		if (ea == ExtendedActions.DOWN_SPEED_JUMP) 
		{
			action[ExtendedActions.DOWN.ordinal()] = true;
			action[ExtendedActions.JUMP.ordinal()] = true;
			action[ExtendedActions.SPEED.ordinal()] = true;
		}
		
		return action;
	}
	
	private void updateQtable() {
		int currentStateIdx = stateTranslation(currentState);
		int prevStateIdx = stateTranslation(prevState);
		double reward = rTable[currentStateIdx][currentAction.ordinal()] + getImmediateReward();
		qTable[prevStateIdx][prevAction.ordinal()] = (1 - ALPHA) * qTable[prevStateIdx][prevAction.ordinal()] + ALPHA * (reward + GAMMA * qTable[currentStateIdx][currentAction.ordinal()]);
	}
}
