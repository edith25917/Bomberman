/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamekernal;

import gamekernal.CommandSolver.CommandWrapper;
import gamekernal.CommandSolver.MouseCommandListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Edith
 */
public class CommandSolver extends Thread {

    public enum MouseState {
        CLICKED,
        PRESSED,
        RELEASED,
        ENTERED,
        EXITED,
        WHEEL_MOVED,
        DRAGGED,
        MOVED
    }

    private static class Data {

        protected long time;

        public Data(long time) {
            this.time = time;
        }
    }

    private static class KeyData extends Data {

        private Map<Byte, Boolean> map;
        private char key;

        public KeyData(Map<Byte, Boolean> map, long time) {
            super(time);
            this.map = map;
            this.key = 65535;
        }

        public KeyData(Map<Byte, Boolean> map, char key, long time) {
            super(time);
            this.map = map;
            this.key = key;
        }
    }

    private static class MouseData extends Data {

        private MouseEvent e;
        private MouseState state;

        public MouseData(MouseEvent e, MouseState state, long time) {
            super(time);
            this.e = e;
            this.state = state;
        }
    }

    public interface KeyCommandListener {

        public void keyPressed(int commandCode, long trigTime);

        public void keyReleased(int commandCode, long trigTime);
    }

    public interface TypedListener {

        public void keyTyped(char c, long trigTime);
    }

    public interface KeyListener extends KeyCommandListener, TypedListener {
    }

    public interface MouseCommandListener {

        public void mouseTrig(MouseEvent e, MouseState state, long trigTime);
    }

    public static class CommandWrapper {

        KeyData keyData;
        MouseData mouseData;

        private CommandWrapper(KeyData keyData, MouseData mouseData) {
            this.keyData = keyData;
            this.mouseData = mouseData;
        }

        public void actionCommand(KeyCommandListener listener) {
            if (listener == null || keyData == null) {
                return;
            }
            keyData.map.keySet().forEach((key) -> {
                boolean pressed = keyData.map.get(key);
                if (pressed) {
                    listener.keyPressed(key, keyData.time);
                } else {
                    listener.keyReleased(key, keyData.time);
                }
            });
        }

        public void actionCommand(MouseCommandListener listener) {
            if (listener == null || mouseData == null) {
                return;
            }
            listener.mouseTrig(mouseData.e, mouseData.state, mouseData.time);
        }

        public void actionCommand(TypedListener listener) {
            if (listener == null || keyData == null || keyData.key == 65535) {
                return;
            }
            listener.keyTyped(keyData.key, keyData.time);
        }

        public void actionCommand(KeyListener listener) {
            actionCommand((TypedListener) listener);
            actionCommand((KeyCommandListener) listener);
        }
    }

    public static class CommandConverter {

        // add @ 20191018 start
        private boolean isTrackChar;
        private char currentChar;
        // add @ 20191018 end

        private boolean clear;// ��������銝�撟���誘
        private boolean isKeyDeletion;// ��������潮摰皜(銝�孛�released)
        private final Map<Integer, Byte> keyMap;// input to command
        private Map<Byte, Boolean> pressedMap;// command pressed/released

        public CommandConverter(boolean clear, boolean isKeyDeletion, boolean isTrackChar) {
            keyMap = new ConcurrentHashMap<>();
            pressedMap = new ConcurrentHashMap<>();
            this.clear = clear;
            this.isKeyDeletion = isKeyDeletion;
            this.isTrackChar = isTrackChar;
            if (isTrackChar) {
                currentChar = 65535;
            }
        }

        public void addKeyPair(int key, Byte command) {
            keyMap.put(key, command);
        }

        public void addKeyPair(int key, int command) {
            keyMap.put(key, (byte) command);
        }

        public void updateCommandByKey(int key, boolean pressed) {
            if (isTrackChar) {
                if (pressed && key >= 0 && key <= 255) {
                    currentChar = (char) key;
                } else {
                    currentChar = (char) 65535;
                }
            }
            if (!keyMap.containsKey(key)) {
                return;
            }
            pressedMap.put(keyMap.get(key), pressed);
        }

        public boolean getPressedByKey(int key) {
            if (!keyMap.containsKey(key)) {
                return false;
            }
            return pressedMap.get(keyMap.get(key));
        }

        private Map<Byte, Boolean> getCurrentMap() {
            Map<Byte, Boolean> tmp = pressedMap;
            if (clear) {
                pressedMap = new ConcurrentHashMap<>();
            } else {
                pressedMap = new ConcurrentHashMap<>(tmp);
                if (!isKeyDeletion) {
                    // fill map
                    keyMap.values().forEach((value) -> {
                        if (!tmp.containsKey(value)) {
                            tmp.put(value, Boolean.FALSE);
                        }
                    });
                } else {
                    // remove false
                    keyMap.values().forEach((value) -> {
                        if (pressedMap.containsKey(value) && !pressedMap.get(value)) {
                            pressedMap.remove(value);
                        }
                    });
                }
            }

            return tmp;
        }

        private char getCurrentKey() {
            char tmp = currentChar;
            currentChar = 65535;
            return tmp;
        }

        public KeyData release(long currentTime) {
            Map<Byte, Boolean> t = getCurrentMap();
            if (isTrackChar) {
                return new KeyData(t, getCurrentKey(), currentTime);
            }
            return new KeyData(t, currentTime);
        }

        public KeyData release() {
            return release(0);
        }
    }

    private static class CommandRecorder<T extends Data> {

        private class Node {

            private T data;
            private Node next;

            public Node(T data) {
                this.data = data;
            }
        }
        private Node root;
        private Node current;
        private Node tail;

        public CommandRecorder() {
            root = tail = current = new Node(null);
        }

        public void add(T data) {
            tail = tail.next = new Node(data);
        }

        public boolean hasNext() {
            return current != tail;
        }

        public T next() {
            current = current.next;
            return current.data;
        }

        public T getCurrent() {
            return current.data;
        }

        public void reset() {
            current = root;
        }

//        public void writeCsv(String fileName){
//            
//        }
//        
//        public static CommandRecorder genByCsv(String fileName){
//            
//        }
    }

    private static class MouseTracker {

        private CommandRecorder<MouseData> recorder;
        private MouseEvent currentEvent;
        private MouseState currentState;

        private MouseTracker() {
            recorder = new CommandRecorder<>();
        }

        private void trig(MouseEvent e, MouseState state) {
            currentEvent = e;
            currentState = state;
        }

        // 撠����誘摮recorder銝血�(皛�����)��誘���
        private void record(long time) {
            recorder.add(new MouseData(currentEvent, currentState, time));
            currentEvent = null;
            currentState = null;
        }

        // �����������誘
        public MouseData update() {
            if (recorder.hasNext()) {
                return recorder.next();
            }
            return null;
        }

        public void bind(Component c) {
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    trig(e, MouseState.CLICKED);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    trig(e, MouseState.PRESSED);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    trig(e, MouseState.RELEASED);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    trig(e, MouseState.ENTERED);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    trig(e, MouseState.EXITED);
                }

                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    trig(e, MouseState.WHEEL_MOVED);
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    trig(e, MouseState.DRAGGED);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    trig(e, MouseState.MOVED);
                }
            };
            c.addMouseListener(mouseAdapter);
            c.addMouseMotionListener(mouseAdapter);
            c.addMouseWheelListener(mouseAdapter);
        }
    }

    private static class KeyTracker {
        // �摰儔銵��”

        private CommandConverter commandList;
        // 蝝�����甈⊥�����銵
        private CommandRecorder<KeyData> recorder;

        // 撠銵��”
        private KeyTracker(boolean clear, boolean isKeyDeletion, boolean isTrackChar) {
            commandList = new CommandConverter(clear, isKeyDeletion, isTrackChar);
            recorder = new CommandRecorder<>();
        }

        // �憓摰儔��隞亙�����誘 => ��銝�車憿�nput閮剖��
        public void add(int key, int command) {
            commandList.addKeyPair(key, command);
        }

        // ��摰儔���������誘�����
        public void trig(int key, boolean isPressed) {
            commandList.updateCommandByKey(key, isPressed);
        }

        // 撠����誘摮recorder銝血���誘���
        public void record(long time) {
            recorder.add(commandList.release(time));
        }

        // �����������誘
        public KeyData update() {
            if (recorder.hasNext()) {
                return recorder.next();
            }
            return null;
        }

        public void bind(Component c) {
            c.addKeyListener(new java.awt.event.KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    trig(e.getKeyCode(), true);
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    trig(e.getKeyCode(), false);
                }
            });
            c.setFocusable(true);
            c.requestFocusInWindow();
        }
    }

    public static class Builder {

        private ArrayList<int[]> cmArray;
        private int[][] array;
        private long deltaTime;
        private MouseTracker mt;
        private boolean isKeyTracker;
        private boolean clear;
        private boolean isKeyDeletion;
        private boolean isTrackChar;
        private MouseCommandListener ml;
        private KeyListener kl;
        private Component c;//mt.bind(c);

        private void init(Component c, long deltaTime) {
            this.c = c;
            this.deltaTime = deltaTime;
            isKeyTracker = false;
            clear = false;
        }

        public Builder(Component c, long deltaTime) {
            init(c, deltaTime);
            cmArray = new ArrayList<>();
        }

        public Builder(Component c, long deltaTime, int[][] array) {
            init(c, deltaTime);
            this.array = array;
        }

        public Builder(Component c, long deltaTime, ArrayList<int[]> cmArray) {
            init(c, deltaTime);
            this.cmArray = cmArray;
        }

        public Builder add(int key, int command) {
            cmArray.add(new int[]{key, command});
            return this;
        }

        public Builder enableMouseTrack(MouseCommandListener ml) {
            this.ml = ml;
            mt = new MouseTracker();
            mt.bind(c);
            return this;
        }

        public Builder enableKeyboardTrack(KeyListener kl) {
            this.kl = kl;
            isKeyTracker = true;
            return this;
        }

        public Builder enableMouseTrack() {
            mt = new MouseTracker();
            mt.bind(c);
            return this;
        }

        public Builder enableKeyboardTrack() {
            isKeyTracker = true;
            return this;
        }

        public Builder keyTypedMode() {
            clear = true;
            return this;
        }

        public Builder keyCleanMode() {
            // if keyTyped Mode open, clean mode banned
            if (!clear) {
                isKeyDeletion = true;
            }
            return this;
        }

        public Builder trackChar() {
            isTrackChar = true;
            return this;
        }

        public CommandSolver gen() {
            KeyTracker kt = null;
            if (isKeyTracker) {
                kt = new KeyTracker(clear, isKeyDeletion, isTrackChar);
                if (cmArray != null) {
                    for (int[] keyPair : cmArray) {
                        kt.add(keyPair[0], keyPair[1]);
                    }
                } else if (array != null) {
                    for (int[] keyPair : array) {
                        kt.add(keyPair[0], keyPair[1]);
                    }
                }
                kt.bind(c);
            }

            CommandSolver cs = new CommandSolver(deltaTime, kt, mt);
            cs.observeKeyBoard(kl);
            cs.observeMouse(ml);

            return cs;
        }
    }

    private final long deltaTime;
    private final KeyTracker keyTracker;
    private final MouseTracker mouseTracker;
    private MouseCommandListener ml;
    private KeyListener kl;

    private CommandSolver(long deltaTime, KeyTracker keyTracker, MouseTracker mouseTracker) {
        this.deltaTime = deltaTime;
        this.keyTracker = keyTracker;
        this.mouseTracker = mouseTracker;
    }

    public void observeMouse(MouseCommandListener ml) {
        this.ml = ml;
    }

    public void observeKeyBoard(KeyListener kl) {
        this.kl = kl;
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        long passedFrames = 0;
        while (true) {
            long currentTime = System.nanoTime();
            long totalTime = currentTime - startTime;
            long targetTotalFrames = totalTime / deltaTime;
            while (passedFrames < targetTotalFrames) {
                if (keyTracker != null) {
                    synchronized (keyTracker) {
                        keyTracker.record(currentTime);
                    }
                }
                if (mouseTracker != null) {
                    mouseTracker.record(currentTime);
                }
                passedFrames++;
            }
        }
    }

    public CommandWrapper update() {
        MouseData mouseData = null;
        KeyData keyData = null;
        if (keyTracker != null) {
            synchronized (keyTracker) {
                keyData = keyTracker.update();
            }
        }
        if (mouseTracker != null) {
            synchronized (mouseTracker) {
                mouseData = mouseTracker.update();
            }
        }

        CommandWrapper cw = new CommandWrapper(keyData, mouseData);
        if (kl != null) {
            cw.actionCommand(kl);
        }
        if (ml != null) {
            cw.actionCommand(ml);
        }

        return cw;
    }
    
}
