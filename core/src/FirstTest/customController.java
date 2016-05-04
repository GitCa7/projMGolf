package FirstTest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class customController extends GestureDetector implements InputProcessor{
	
	/** The button for translating the camera along the up/right plane */
	public int rotatebutton = Buttons.RIGHT;
	public boolean rotatebuttonpressed;
	public int placeDragButton = Keys.SPACE;
	public boolean placeDragButtonpressed;
	public int forwardButton = Buttons.MIDDLE;
	public boolean forwardButtonpressed;
	
	
	
	/** The angle to rotate when moved the full width or height of the screen. */
	public float rotateAngle = 360f;
	/** The units to translate the camera when moved the full width or height of the screen. */
	public float translateUnits = 10f; // FIXME auto calculate this based on the target
	/** The key which must be pressed to activate rotate, translate and forward or 0 to always activate. */
	public int activateKey = 0;
	/** Indicates if the activateKey is currently being pressed. */
	protected boolean activatePressed;
	/** Whether scrolling requires the activeKey to be pressed (false) or always allow scrolling (true). */
	public boolean alwaysScroll = true;
	/** The weight for each scrolled amount. */
	public float scrollFactor = -0.1f;
	/** World units per screen size */
	public float pinchZoomFactor = 10f;
	/** Whether to update the camera after it has been changed. */
	public boolean autoUpdate = true;
	/** The target to rotate around. */
	public Vector3 target = new Vector3();
	/** Whether to update the target on translation */
	public boolean translateTarget = true;
	/** Whether to update the target on forward */
	public boolean forwardTarget = true;
	/** Whether to update the target on scroll */
	public boolean scrollTarget = false;
	
	
	public int moveForwardButton = Keys.W;
	protected boolean moveForwardPressed;
	public int moveRightButton = Keys.D;
	protected boolean moveRightPressed;
	public int moveLeftButton = Keys.A;
	protected boolean moveLeftPressed;
	public int moveBackButton = Keys.S;
	protected boolean moveBackPressed;
	
	
	public float clickedX, clickedY;
	
	
	public Camera camera;
	/** The current (first) button being pressed. */
	protected int button = -1;

	private float startX, startY;
	private final Vector3 tmpV1 = new Vector3();
	private final Vector3 tmpV2 = new Vector3();
	
	
	
	
	protected static class CameraGestureListener extends GestureAdapter {
		public customController controller;
		private float previousZoom;

		@Override
		public boolean touchDown (float x, float y, int pointer, int button) {
			previousZoom = 0;
			return false;
		}

		@Override
		public boolean tap (float x, float y, int count, int button) {
			return false;
		}

		@Override
		public boolean longPress (float x, float y) {
			return false;
		}

		@Override
		public boolean fling (float velocityX, float velocityY, int button) {
			return false;
		}

		@Override
		public boolean pan (float x, float y, float deltaX, float deltaY) {
			return false;
		}

		@Override
		public boolean zoom (float initialDistance, float distance) {
			float newZoom = distance - initialDistance;
			float amount = newZoom - previousZoom;
			previousZoom = newZoom;
			float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
			return controller.pinchZoom(amount / ((w > h) ? h : w));
		}

		@Override
		public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
			return false;
		}
	}

	protected final CameraGestureListener gestureListener;
	
	protected customController (final CameraGestureListener gestureListener, final Camera camera) {
		super(gestureListener);
		this.gestureListener = gestureListener;
		this.gestureListener.controller = this;
		this.camera = camera;
		clickedX = 0;
		clickedY = 0;
	}
	
	protected customController(final Camera camera) {
		this(new CameraGestureListener(), camera);
		clickedX = 0;
		clickedY = 0;
	}
	
	public void update(){
		
		
		if(moveRightPressed || moveLeftPressed || moveBackPressed || moveForwardPressed)	{
			
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		
		clickedX = Gdx.input.getX();
		clickedY = Gdx.input.getY();
		
		if (keycode == activateKey) activatePressed = true;
		if (keycode == placeDragButton)	{
			placeDragButtonpressed = true;
		}
		else if (keycode == moveBackButton)	{
			moveBackPressed = true;
		}
		else if (keycode == moveRightButton)	{
			moveRightPressed = true;
		}
		else if (keycode == moveLeftButton) 	{
			moveLeftPressed = true;
		}
		return false;
	}

	public boolean pinchZoom(float amount) {
		return zoom(pinchZoomFactor * amount);
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == activateKey) activatePressed = true;
		if (keycode == placeDragButton)	{
			placeDragButtonpressed = false;
		}
		else if (keycode == moveBackButton)
			moveBackPressed = false;
		else if (keycode == moveRightButton)
			moveRightPressed = false;
		else if (keycode == moveLeftButton) 
			moveLeftPressed = false;
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	private int touched;
	private boolean multiTouch;

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		
		touched |= (1 << pointer);
		multiTouch = !MathUtils.isPowerOfTwo(touched);
		if (multiTouch)
			this.button = -1;
		else if (this.button < 0 && (activateKey == 0 || activatePressed)) {
			startX = screenX;
			startY = screenY;
			this.button = button;
		}
		return super.touchDown(screenX, screenY, pointer, button) || (activateKey == 0 || activatePressed);
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		touched &= -1 ^ (1 << pointer);
		multiTouch = !MathUtils.isPowerOfTwo(touched);
		if (button == this.button) this.button = -1;
		return super.touchUp(screenX, screenY, pointer, button) || activatePressed;
	}

	protected boolean process (float deltaX, float deltaY, int button) {
		if (button == rotatebutton) {
			tmpV1.set(camera.direction).crs(camera.up).y = 0f;
			camera.rotateAround(target, tmpV1.nor(), deltaY * rotateAngle);
			camera.rotateAround(target, Vector3.Y, deltaX * -rotateAngle);
			
		} else if (button == placeDragButton) {
			//When clicking, has to return coordinates for now
			
			/*
			camera.translate(tmpV1.set(camera.direction).crs(camera.up).nor().scl(-deltaX * translateUnits));
			camera.translate(tmpV2.set(camera.up).scl(-deltaY * translateUnits));
			if (translateTarget) target.add(tmpV1).add(tmpV2);
			*/
		} else if (button == forwardButton) {
			camera.translate(tmpV1.set(camera.direction).scl(deltaY * translateUnits));
			
			if (forwardTarget) target.add(tmpV1);
		}
		if (autoUpdate) camera.update();
		return true;
	}
	
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		boolean result = super.touchDragged(screenX, screenY, pointer);
		if (result || this.button < 0) return result;
		final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
		final float deltaY = (startY - screenY) / Gdx.graphics.getHeight();
		startX = screenX;
		startY = screenY;
		return process(deltaX, deltaY, button);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return zoom(10* amount * scrollFactor * translateUnits);
	}
	
	public boolean zoom (float amount) {
		if (!alwaysScroll && activateKey != 0 && !activatePressed) return false;
		camera.translate(tmpV1.set(camera.direction).scl(amount));
		if (scrollTarget) target.add(tmpV1);
		if (autoUpdate) camera.update();
		return true;
	}
	
	public float getxClicked()	{
		return clickedX;
	}

	public float getyClicked()	{
		return clickedY;
	}
}
