import java.awt.*;
import javax.swing.*;

public class GUIproject1 {

    public static void main(String[] args) {
        // 1. Crear la ventana principal
       JFrame ventana = new JFrame("Mi Aplicación");
        
        ventana.setSize(400, 300);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);   // centrar en pantalla

        // 2. Crear un panel con FlowLayout
        JPanel panel = new JPanel(new FlowLayout());

        // 3. Crear componentes
        JLabel  etiqueta = new JLabel("Ingresá tu nombre:");
        JTextField campo = new JTextField(15);
        JButton  boton   = new JButton("Saludar");
        JLabel  resultado = new JLabel("");

        // 4. Asociar evento al botón
        boton.addActionListener(e -> {
            String nombre = campo.getText();
            resultado.setText("¡Hola, " + nombre + "!");
        });

        // 5. Agregar componentes al panel
        panel.add(etiqueta);
        panel.add(campo);
        panel.add(boton);
        panel.add(resultado);

        // 6. Agregar panel a la ventana y mostrar
        ventana.add(panel);
        ventana.setVisible(true);
      
    }
}