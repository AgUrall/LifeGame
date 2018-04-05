package GM; /**
 * Created by Михаил on 11.12.2016.
 */


import Model.Cell;
import view.BaseView;
import Model.Cell;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Управляет логикой игры, добавлением шариков, обсчетом их движения, перерисовкой вьюшки
 */
public class GameManager implements BaseView.EventsListener {
    // =============================================================================================
    // FIELDS
    // =============================================================================================
    private final BaseView view;
    // Ширина и высота вьюшки
    private int width;
    private int height;

    private Cell[][] cellsArray = new Cell[100][100];
    private Cell[][] nextcellsArray = new Cell[100][100];

    // Запущена ли игра
    private boolean isRunning;
    private boolean isPaused;
    // Frames per second - обновлений вьюшки в секунду
    private int fps;
    // Updates per second - обновлений мира в секунду
    private int ups;

    // =============================================================================================
    // CONSTRUCTOR
    // =============================================================================================
    public GameManager(BaseView _view) {
        view = _view;
        view.setEventsListener(this);
        for (int y = 0; y < 100;y++)
            for (int x = 0; x < 100;x++) {
                cellsArray[y][x] = new Cell(x,y);
                nextcellsArray[y][x] = new Cell(x,y);


        }
    }

    // =============================================================================================
    // METHODS
    // =============================================================================================

    /**
     * Запустить игру
     */
    public void start() {
        // Создаем новый поток, назначаем ему функцию и запускаем его
        new Thread(threadRunnable).start();
        isPaused = true;
    }

    /**
     * Приостановить игру
     */
    public void stop() {
        // Поток умрет сам, после того как isRunning станет false
        isRunning = false;
    }
    public void pauseGame() {
        // Поток умрет сам, после того как isRunning станет false
        isPaused = false;
    }

    /**
     * Функция которую исполняет поток. Она определяет когда следует вызвать пересчет мира, а когда его отрисовку.
     * Вообще говоря, внутри метода run() творится форменная магия, смысл которой вам понимать
     * не обязательно, для тех кому интересно гуглим "game loop", вот ссылки которыми руководствовался я
     * http://gameprogrammingpatterns.com/game-loop.html
     * http://entropyinteractive.com/2011/02/game-engine-design-the-game-loop/
     * http://www.java-gaming.org/index.php?topic=24220.0
     */
    private final Runnable threadRunnable = new Runnable() {
        @Override
        public void run() {
            isRunning = true;
            long lastTime = System.nanoTime();
            double delta = 0.0;
            double ns = 1000000000.0 / 60.0;
            long timer = System.currentTimeMillis();
            int updates = 0;
            int frames = 0;
            while (isRunning) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                if (delta >= 10.0) {
                    if(!isPaused){update();
                    updates++;}
                    delta--;
                }
                render();
                frames++;
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    ups = updates;
                    fps = frames;
                    updates = 0;
                    frames = 0;
                }
            }
        }
    };

    /**
     * Сменить состояние клетки
     */
    public void switchCell(int x, int y) {
        //if (isRunning) return;
        cellsArray[y][x].switchState();
    }

    // =============================================================================================
    // UPDATING
    // =============================================================================================

    /**
     * Пересчет мира, положения столкновения и прочая физика
     */
    private void update() {
        for (int y = 0; y < 100; y++)
            for (int x = 0; x < 100; x++)
                nextcellsArray[y][x].setState(cellsArray[y][x].getState());

        for (int y = 0; y < 100; y++){
            for (int x = 0; x < 100; x++) {
                boolean willAlive = cellsArray[y][x].getState() != 0;
                boolean wasAlive = willAlive;
                int cuntAlive = 0;
                //System.err.print(((y - 1 + 100) % 100) + "|" + ((y + 1) % 100) + "|" +((((y - 1 + 100) % 100) + 1) % 100) );
                /*for (int y1 = ((y - 1 + 100) % 100); y1 == ((y + 1) % 100); y1= ((y1 ++) % 100)) {
                    for (int x1 = ((x - 1 + 100) % 100); x1 == ((x + 1) % 100); x1 = ((x1 ++) % 100)) {
                        if (cellsArray[y1][x1].getState() == 1) cuntAlive++;
                    }
                }*/

                if (cellsArray[(y + 99) % 100][(x + 99) % 100].getState() == 1) cuntAlive++;
                if (cellsArray[(y + 99) % 100][x].getState() == 1) cuntAlive++;
                if (cellsArray[(y + 99) % 100][(x + 1) % 100].getState() == 1) cuntAlive++;
                if (cellsArray[y][(x - 1 + 100) % 100].getState() == 1) cuntAlive++;
                if (cellsArray[y][(x + 1 + 100) % 100].getState() == 1) cuntAlive++;
                if (cellsArray[(y + 1) % 100][(x + 99) % 100].getState() == 1) cuntAlive++;
                if (cellsArray[(y + 1) % 100][x].getState() == 1) cuntAlive++;
                if (cellsArray[(y + 1) % 100][(x + 1) % 100].getState() == 1) cuntAlive++;

                //for (int y1 =

                //if (cellsArray[y][x].getState() == 1) cuntAlive--;
                if (!wasAlive && (cuntAlive == 3)) {
                    nextcellsArray[y][x].switchState();

                }

                if (wasAlive && (cuntAlive < 2 || cuntAlive > 3)) {
                    nextcellsArray[y][x].switchState();

                    if (nextcellsArray[y][x].getState() == cellsArray[y][x].getState());
                }

                //if(wasAlive != willAlive){ nextcellsArray[y][x].switchState(); }

            }
        }
        for (int y = 0; y < 100; y++)
            for (int x = 0; x < 100; x++)
                cellsArray[y][x].setState(nextcellsArray[y][x].getState());
    }

    /**
     * Отрисовка мира
     */
    private void render() {
        view.repaint();
    }

    @Override
    public void onDraw(Graphics g) {
        // Отрисовываем каждый шарик
        g.drawRect(0,0,1000,1000);
        for(int y = 0; y < 1000; y+=10)
            for(int x = 0; x < 1000; x+=10) {
                g.drawLine(0, y, 1000, y);
                g.drawLine(x, 0, x, 1000);
            }


        for (Cell[] cells : cellsArray) {
            for (Cell cell : cells)
                if(cell.getState() == 1) {
                    g.setColor(Color.GREEN);
                    g.fillRect(cell.x * 10, cell.y * 10, 10, 10);
                }
        }

        // Вывод служебной информации
        g.setColor(Color.BLACK);
        g.drawString(String.format("ups=%d, fps=%d", ups, fps), 5, 16);
    }

     @Override
     public void onResize(int _width, int _height) {
     // При изменении размера вьюшки просто запоминаем новые значения, они нужны для обсчета колизий
     width = _width;
     height = _height;
     }


}

