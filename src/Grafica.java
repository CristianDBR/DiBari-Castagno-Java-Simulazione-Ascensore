import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class Grafica extends JPanel implements ActionListener {
    private static final int NUM_PIANI = 10;
    private static final int LARGHEZZA = 400;
    private static final int ALTEZZA = 600;
    private static final int DIM_PERSONA = 15; 
    private static final int VEL_MOVIMENTO = 5; 
    private static final int SIM_CYCLE_INTERVAL = 20;
    private static final int DURATA_SIMULAZIONE = 100;
    
    private Ascensore ascensore;
    private Piano[] piani;
    private int idContatore = 1;
    
    private Timer timer;
    private int tickCounter = 0;
    private int simulationCycle = 1;
    private int posizioneAscensore; 
    private double doorProgress = 0.0;

    public Grafica() {
        setPreferredSize(new Dimension(LARGHEZZA, ALTEZZA));
        
        piani = new Piano[NUM_PIANI];
        for (int i = 0; i < NUM_PIANI; i++) {
            piani[i] = new Piano(i + 1);
        }
        ascensore = new Ascensore(5);
        posizioneAscensore = ALTEZZA - (ascensore.getPianoCorrente() * (ALTEZZA / NUM_PIANI));
        
        timer = new Timer(50, this);
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        drawBuilding(g2d);
        drawElevator(g2d);
    }
    
    private void drawBuilding(Graphics2D g2d) {
        int pianoHeight = ALTEZZA / NUM_PIANI;

        GradientPaint gpArea = new GradientPaint(0, 0, new Color(245, 245, 245),
                                                 0, ALTEZZA, new Color(220, 220, 220));
        g2d.setPaint(gpArea);
        g2d.fillRect(0, 0, 100, ALTEZZA);

        for (int i = 0; i < NUM_PIANI; i++) {
            int y = ALTEZZA - ((i + 1) * pianoHeight);

            RoundRectangle2D.Float pianoRect = new RoundRectangle2D.Float(10, y + 5, 80, pianoHeight - 10, 15, 15);
            GradientPaint gpPiano = new GradientPaint(10, y, Color.WHITE,
                                                      10, y + pianoHeight, new Color(200, 200, 255));
            g2d.setPaint(gpPiano);
            g2d.fill(pianoRect);

            g2d.setColor(Color.DARK_GRAY);
            g2d.draw(pianoRect);

            g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2d.setColor(Color.BLACK);
            g2d.drawString("Piano " + (i + 1), 15, y + pianoHeight / 2);

            int xOffset = 15;
            for (Persona p : piani[i].getCodaPersone()) {
                drawStickman(g2d, xOffset, y + 10, DIM_PERSONA, new Color(180, 0, 180));
                xOffset += DIM_PERSONA + 5;
            }
        }
    }
    
    private void drawElevator(Graphics2D g2d) {
        int pianoHeight = ALTEZZA / NUM_PIANI;
        int elevatorX = 180;
        int elevatorY = posizioneAscensore;
        int elevatorWidth = 60;
        int elevatorHeight = pianoHeight;
        
        GradientPaint gpElevator = new GradientPaint(elevatorX, elevatorY, new Color(180, 180, 180),
                                                      elevatorX, elevatorY + elevatorHeight, new Color(120, 120, 120));
        g2d.setPaint(gpElevator);
        g2d.fillRect(elevatorX, elevatorY, elevatorWidth, elevatorHeight);
        
        g2d.setColor(Color.BLACK);
        g2d.drawRect(elevatorX, elevatorY, elevatorWidth, elevatorHeight);
        
        if (ascensore.isPorteAperte()) {
            doorProgress = Math.min(doorProgress + 0.05, 1.0);
        } else {
            doorProgress = Math.max(doorProgress - 0.05, 0.0);
        }
        
        int doorWidth = 20;
        int doorHeight = elevatorHeight - 10;
        int doorOffset = (int)(doorProgress * 15);
        
        int leftDoorX = elevatorX + 10 - doorOffset;
        int doorY = elevatorY + 5;
        g2d.setColor(new Color(100, 100, 100));
        g2d.fillRect(leftDoorX, doorY, doorWidth, doorHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(leftDoorX, doorY, doorWidth, doorHeight);
        
        int rightDoorX = elevatorX + 30 + doorOffset;
        g2d.setColor(new Color(100, 100, 100));
        g2d.fillRect(rightDoorX, doorY, doorWidth, doorHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(rightDoorX, doorY, doorWidth, doorHeight);
        
        int xOffset = elevatorX + 5;
        for (Persona p : ascensore.getPersoneDentro()) {
            drawStickman(g2d, xOffset, elevatorY + 10, DIM_PERSONA, Color.ORANGE);
            xOffset += DIM_PERSONA + 5;
        }
    }
    
    private void drawStickman(Graphics2D g2d, int x, int y, int size, Color color) {
        int headSize = size; 
        int bodyHeight = size + 5;
        int armLength = size / 2;
        int legLength = size / 2;
        
        g2d.setColor(new Color(50, 50, 50, 100));
        g2d.fillOval(x + 2, y + 2, headSize, headSize);
        g2d.setColor(color);
        g2d.fillOval(x, y, headSize, headSize);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x, y, headSize, headSize);
        
        int bodyStartX = x + headSize / 2;
        int bodyStartY = y + headSize;
        int bodyEndY = bodyStartY + bodyHeight;
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(bodyStartX, bodyStartY, bodyStartX, bodyEndY);
        
        int armY = bodyStartY + bodyHeight / 3;
        g2d.drawLine(bodyStartX - armLength, armY, bodyStartX + armLength, armY);
        
        g2d.drawLine(bodyStartX, bodyEndY, bodyStartX - legLength, bodyEndY + legLength);
        g2d.drawLine(bodyStartX, bodyEndY, bodyStartX + legLength, bodyEndY + legLength);
        g2d.setStroke(new BasicStroke(1));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        tickCounter++;
        int pianoHeight = ALTEZZA / NUM_PIANI;
        int targetY = ALTEZZA - (ascensore.getPianoCorrente() * pianoHeight);
        
        if (posizioneAscensore < targetY) {
            posizioneAscensore = Math.min(posizioneAscensore + VEL_MOVIMENTO, targetY);
        } else if (posizioneAscensore > targetY) {
            posizioneAscensore = Math.max(posizioneAscensore - VEL_MOVIMENTO, targetY);
        }
        
        if (tickCounter % SIM_CYCLE_INTERVAL == 0) {
            if (simulationCycle > DURATA_SIMULAZIONE) {
                timer.stop();
                System.out.println("Simulazione terminata.");
                return;
            }
            System.out.println("===== CICLO " + simulationCycle + " =====");
            
            if (simulationCycle % 2 == 0 && Math.random() < 0.75) {
                int pianoPartenza = (int) (Math.random() * NUM_PIANI) + 1;
                Persona nuovaPersona = new Persona(idContatore++, pianoPartenza);
                piani[pianoPartenza - 1].aggiungiPersonaCoda(nuovaPersona);
                System.out.println("Nuova persona al piano " + pianoPartenza);
            }
            
            for (Piano piano : piani) {
                if (!piano.getCodaPersone().isEmpty()) {
                    if (piano.getNumeroPiano() == ascensore.getPianoCorrente() && ascensore.isPorteAperte()) {
                        piano.resettaTempoAttesa();
                    } else {
                        piano.incrementaTempoAttesa();
                    }
                }
            }
            
            boolean richiestaCorrente = !piani[ascensore.getPianoCorrente() - 1].getCodaPersone().isEmpty();
            for (Persona p : ascensore.getPersoneDentro()) {
                if (p.getPianoDestinazione() == ascensore.getPianoCorrente()) {
                    richiestaCorrente = true;
                    break;
                }
            }
            if (richiestaCorrente) {
                ascensore.apriPorte();
            } else {
                System.out.println("[ASC] Nessuna richiesta al piano " + ascensore.getPianoCorrente() + ". Porte rimangono chiuse.");
            }
            
            if (ascensore.isPorteAperte()) {
                while (ascensore.isPorteAperte() &&
                       !piani[ascensore.getPianoCorrente() - 1].getCodaPersone().isEmpty()) {
                    Persona p = piani[ascensore.getPianoCorrente() - 1].rimuoviPersonaCoda();
                    ascensore.aggiungiPersona(p);
                    System.out.println("Persona " + p.getId() + " entra nell'ascensore.");
                }
            }
            
            ascensore.rimuoviPersoneArrivate();
            
            if (ascensore.isPorteAperte()) {
                ascensore.chiudiPorte();
            }
            
            ascensore.decidiDirezione(piani);
            
            simulationCycle++;
        }
        
        repaint();
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simulazione Ascensore - Grafica Smooth");
        Grafica panel = new Grafica();
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
