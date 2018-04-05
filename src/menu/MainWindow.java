package menu ;

import javax.swing .*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import GM.GameManager;

import view.BaseView;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

/*z*
 * Главное окно игры
 */
public class MainWindow {
       private MainWindow() {
        // Инициализируем свойства окна
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setBounds(z0, 0, 1000, 1000);
        // Приложение должно завершиться после закрытия окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("игра жизнь"  );

        //  Создаем вьюшку и добавляем ее в окно
        BaseView view = new BaseView();
        frame.getContentPane().add(view);

        // Создаем мэнеджер управляющий логикой игры
        final GameManager gameManager = new GameManager(view);

        // Добавляем верхнее меню для старта, паузы игры
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        final JMenuItem startMenuItem = new JMenuItem("Pause Game");
        menuBar.add(startMenuItem);
        final JMenuItem pauseMenuItem = new JMenuItem("Stop Game");
        pauseMenuItem.setEnabled(false);
        menuBar.add(pauseMenuItem);
        final JMenuItem pause2MenuItem = new JMenuItem("Start");
        pause2MenuItem.setEnabled(true);
        menuBar.add(pause2MenuItem);

        // Подписываемся на клики по меню
        startMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.start();
                startMenuItem.setEnabled(false);
                pauseMenuItem.setEnabled(true);
                pause2MenuItem.setEnabled(true);
            }
        });
        pauseMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.stop();
                startMenuItem.setEnabled(true);
                pauseMenuItem.setEnabled(false);
                pause2MenuItem.setEnabled(false);
            }
        });

        pause2MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.pauseGame();
                startMenuItem.setEnabled(true);
                pause2MenuItem.setEnabled(false);
            }
        });

        // Подписываемся на событие клика по вьюшке
        frame.getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // При клике ЛЕВОЙ кнопкой добавляем 1 новый шарик с координатами клика

                if (e.getButton() == MouseEvent.BUTTON1) {
                    gameManager.switchCell((int)(e.getX() /10), (int)(e.getY() /10));
                    //e.getX(), e.getY()
                }
            }
        });
    }

    public static void main(String[] args) {
        // Точка входа, создаем экземпляр главного окна
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow();
            }
        });
    }
}
