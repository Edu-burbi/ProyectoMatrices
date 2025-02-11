import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import models.Cell;

public class App {

    private JFrame frame;
    private JPanel panelMatriz;
    private JButton[][] botones;
    private boolean[][] matriz;
    private boolean modoObstaculos = false;
    private boolean modoSeleccion = false;
    private Cell inicio = null, fin = null;
    private JTextField inputFilas, inputColumnas;
    private String metodoSeleccionado = null;


    public App(int filas, int columnas) {
        inicializarUI(filas, columnas);
    }

    private void inicializarUI(int filas, int columnas) {
        frame = new JFrame("Matriz de Botones");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelMatriz = new JPanel();
        actualizarMatriz(filas, columnas);

        JButton botonObstaculos = new JButton("Ingresar Obstáculos");
        botonObstaculos.addActionListener(e -> {
            modoObstaculos = true;
            modoSeleccion = false;
        });

        JButton botonSeleccion = new JButton("Seleccionar A y B");
        botonSeleccion.addActionListener(e -> {
            modoObstaculos = false;
            modoSeleccion = true;
        });

        JButton botonRecorrido = new JButton("Iniciar Recorrido");
        botonRecorrido.addActionListener(e -> {
            if (metodoSeleccionado == null) {
                JOptionPane.showMessageDialog(frame, "Método no seleccionado. Por favor, elija un método de búsqueda.");
            } else {
                mostrarRutaAnimada();  // Solo se llama si hay un método seleccionado
            }
        });

        JButton botonTiempo = new JButton("Tiempo de Ejecucion");
        botonTiempo.addActionListener(e -> {
        if (metodoSeleccionado == null) {
            JOptionPane.showMessageDialog(frame, "Método no seleccionado. Por favor, elija un método de búsqueda.");
        } else {
            mostrarRuta();  // Solo se llama si hay un método seleccionado
        }
    });

        JButton botonReiniciar = new JButton("Reiniciar");
        botonReiniciar.addActionListener(e -> reiniciar());

        JButton botonSeleccionMetodo = new JButton("Seleccionar Método");
        JPopupMenu menuMetodos = new JPopupMenu();
        String[] metodos = {"DFS", "BFS", "Recursión Simple", "Programación Dinámica"};
        for (String metodo : metodos) {
            JMenuItem item = new JMenuItem(metodo);
            item.addActionListener(e -> metodoSeleccionado = metodo);
            menuMetodos.add(item);
        }
        botonSeleccionMetodo.addActionListener(e -> menuMetodos.show(botonSeleccionMetodo, 0, botonSeleccionMetodo.getHeight()));

        JLabel jlFilas = new JLabel("Filas: ");
        jlFilas.setForeground(Color.WHITE);
        JLabel jlColumnas = new JLabel("Columnas: ");
        jlColumnas.setForeground(Color.white);

        inputFilas = new JTextField(5);
        inputColumnas = new JTextField(5);
        JButton botonActualizar = new JButton("Crear");
        botonActualizar.addActionListener(e -> actualizarMatrizDesdeInput());

        JPanel panelBotones = new JPanel(new GridLayout(3, 2));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelBotones.setBackground(Color.DARK_GRAY);
        panelBotones.add(botonObstaculos);
        panelBotones.add(botonSeleccion);
        panelBotones.add(botonRecorrido);
        panelBotones.add(botonReiniciar);
        panelBotones.add(botonTiempo);
        panelBotones.add(botonSeleccionMetodo);

        JPanel panelBotones2 = new JPanel(new GridLayout(1, 5, 10, 10));
        panelBotones2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelBotones2.setBackground(Color.DARK_GRAY);
        panelBotones2.add(jlFilas);
        panelBotones2.add(inputFilas);
        panelBotones2.add(jlColumnas);
        panelBotones2.add(inputColumnas);
        panelBotones2.add(botonActualizar);

        JPopupMenu menuEmergente = new JPopupMenu();
        JMenuItem recursivo = new JMenuItem("Metodo Recursivo");
        JMenuItem bfs = new JMenuItem("Metodo BFS");
        JMenuItem dp = new JMenuItem("Metodo DP");
        JMenuItem dfs = new JMenuItem("Metodo DFS");

        menuEmergente.add(recursivo);
        menuEmergente.add(bfs);
        menuEmergente.add(dp);
        menuEmergente.add(dfs);

        frame.add(panelBotones2, BorderLayout.NORTH);
        frame.add(panelMatriz, BorderLayout.CENTER);
        frame.add(panelBotones, BorderLayout.SOUTH);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void actualizarMatriz(int filas, int columnas) {
        panelMatriz.removeAll();
        panelMatriz.setLayout(new GridLayout(filas, columnas));
        
        matriz = new boolean[filas][columnas];
        botones = new JButton[filas][columnas];
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = true;
                botones[i][j] = new JButton(" ");
                botones[i][j].setBackground(Color.WHITE);
                final int row = i, col = j;

                botones[i][j].addActionListener(e -> {
                    if (modoObstaculos) {
                        matriz[row][col] = !matriz[row][col];
                        botones[row][col].setBackground(matriz[row][col] ? Color.WHITE : Color.ORANGE);
                    } else if (modoSeleccion) {
                        if (inicio == null) {
                            inicio = new Cell(row, col);
                            botones[row][col].setText("A");
                        } else if (fin == null) {
                            fin = new Cell(row, col);
                            botones[row][col].setText("B");
                        }
                    }
                });
                panelMatriz.add(botones[i][j]);
            }
        }
        panelMatriz.revalidate();
        panelMatriz.repaint();
    }

    private void actualizarMatrizDesdeInput() {
        try {
            int filas = Integer.parseInt(inputFilas.getText());
            int columnas = Integer.parseInt(inputColumnas.getText());
            if (filas > 0 && columnas > 0) {
                actualizarMatriz(filas, columnas);
            } else {
                JOptionPane.showMessageDialog(frame, "Ingrese valores positivos.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Ingrese un número válido.");
        }
    }

    private void mostrarRutaAnimada() {
        List<Cell> ruta = encontrarRutaRecursiva();
        if (ruta.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No se encontró una ruta.");
            return;
        }
    
        SwingWorker<Void, Cell> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (Cell c : ruta) {
                    publish(c);
                    Thread.sleep(300); // Retraso para visualizar el recorrido
                }
                return null;
            }
    
            @Override
            protected void process(List<Cell> chunks) {
                for (Cell c : chunks) {
                    botones[c.row][c.col].setBackground(Color.GREEN);
                }
            }
        };
    
        worker.execute();
    }

    private void mostrarRuta() {
        if (metodoSeleccionado == null) {
            JOptionPane.showMessageDialog(frame, "Método no seleccionado. Por favor, elija un método de búsqueda.");
            return;
        }
    
        List<Cell> ruta = switch (metodoSeleccionado) {
            case "DFS" -> buscarDFS();
            case "BFS" -> buscarBFS();
            case "Recursión Simple" -> encontrarRutaRecursiva();
            case "Programación Dinámica" -> pogramacionDinamica();
            default -> Collections.emptyList();
        };
    
        if (ruta.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No se encontró una ruta.");
        } else {
            for (Cell c : ruta) {
                if (!c.equals(inicio) && !c.equals(fin)) { // Evita cambiar el inicio y fin
                    botones[c.row][c.col].setBackground(new Color(49, 139, 49));
                }
            }
        }
    }

    

    private void reiniciar() {
        actualizarMatriz(matriz.length, matriz[0].length);
        inicio = null;
        fin = null;
        modoObstaculos = false;
        modoSeleccion = false;
    }

    private List<Cell> buscarDFS() {
            return null;
    }

    private List<Cell> buscarBFS() {
            return null;
    }

    private List<Cell> encontrarRutaRecursiva() {
        List<Cell> ruta = new ArrayList<>();
        if (inicio != null && fin != null) {
            boolean[][] visitado = new boolean[matriz.length][matriz[0].length];
            if (buscarRutaRecursiva(inicio.row, inicio.col, ruta, visitado)) {
                return ruta;
            }
        }
        return Collections.emptyList();
    }

    private boolean buscarRutaRecursiva(int row, int col, List<Cell> ruta, boolean[][] visitado) {
        if (row < 0 || col < 0 || row >= matriz.length || col >= matriz[0].length || !matriz[row][col] || visitado[row][col]) {
            return false;
        }
        ruta.add(new Cell(row, col));
        visitado[row][col] = true;
        if (row == fin.row && col == fin.col) return true;
        if (buscarRutaRecursiva(row, col + 1, ruta, visitado) || 
            buscarRutaRecursiva(row + 1, col, ruta, visitado) ||
            buscarRutaRecursiva(row, col - 1, ruta, visitado) || 
            buscarRutaRecursiva(row - 1, col, ruta, visitado)) {
            return true;
        }
        ruta.remove(ruta.size() - 1);
        return false;
    }

    private List<Cell> pogramacionDinamica() {
            return null;
    }

    public static void main(String[] args) {
        new App(1, 1);
    }
}
