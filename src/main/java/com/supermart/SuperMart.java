/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.supermart;

/**
 *
 * @author pabodini
 */

import com.supermart.view.MainFrame;
import javax.swing.SwingUtilities;

public class SuperMart {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);

        });
    }
}