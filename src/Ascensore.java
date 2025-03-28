import java.util.ArrayList;
import java.util.Iterator;

public class Ascensore {
    private int pianoCorrente;
    private int capienzaMassima;
    private ArrayList<Persona> personeDentro;
    private boolean porteAperte;

    // Codici colore ANSI
    public static final String RESET  = "\033[0m";
    public static final String GREEN  = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String CYAN   = "\033[36m";
    // Un codice per l'arancione (usando una palette 256-colors, 208 è un arancione abbastanza brillante)
    public static final String ORANGE = "\033[38;5;208m";

    public Ascensore(int capienzaMassima) {
        this.pianoCorrente = 1; // partenza dal piano 1
        this.capienzaMassima = capienzaMassima;
        this.personeDentro = new ArrayList<>();
        this.porteAperte = false;
    }

    public int getPianoCorrente() {
        return pianoCorrente;
    }

    public boolean isPorteAperte() {
        return porteAperte;
    }
    
    // Getter per i passeggeri a bordo
    public ArrayList<Persona> getPersoneDentro() {
        return personeDentro;
    }

    public void apriPorte() {
        porteAperte = true;
        System.out.println(CYAN + "[ASC] Porte aperte al piano " + pianoCorrente + RESET);
    }

    public void chiudiPorte() {
        porteAperte = false;
        System.out.println(CYAN + "[ASC] Porte chiuse al piano " + pianoCorrente + RESET);
    }

    public void aggiungiPersona(Persona p) {
        if (porteAperte && personeDentro.size() < capienzaMassima) {
            personeDentro.add(p);
            System.out.println(GREEN + "[ASC] " + p + " è salita al piano " + pianoCorrente + RESET);
        } else {
            System.out.println(YELLOW + "[ASC] Impossibile aggiungere " + p.getId() + ": " +
                    (porteAperte ? "Ascensore pieno" : "Porte chiuse") + RESET);
        }
    }

    public void rimuoviPersoneArrivate() {
        Iterator<Persona> iterator = personeDentro.iterator();
        while (iterator.hasNext()) {
            Persona p = iterator.next();
            if (p.getPianoDestinazione() == pianoCorrente && porteAperte) {
                System.out.println(ORANGE + "[ASC] Persona " + p.getId() + " è scesa al piano " + pianoCorrente + RESET);
                iterator.remove();
            }
        }
    }

    public void salita() {
        if (pianoCorrente < 10) {
            pianoCorrente++;
            System.out.println(CYAN + "[ASC] Ascensore in salita: ora è al piano " + pianoCorrente + RESET);
        }
    }

    public void discesa() {
        if (pianoCorrente > 1) {
            pianoCorrente--;
            System.out.println(CYAN + "[ASC] Ascensore in discesa: ora è al piano " + pianoCorrente + RESET);
        }
    }

    /**
     * Schedula il prossimo piano da servire considerando:
     * - Le richieste interne (passeggeri a bordo) con costo = distanza.
     * - Le richieste esterne (piani con persone in coda) con costo = distanza - alpha * tempoAttesa.
     * Viene scelto il piano con il costo (score) minore.
     */
    public void decidiDirezione(Piano[] piani) {
        double alpha = 0.1; // Fattore di ponderazione per il tempo di attesa
        int bestFloor = pianoCorrente;
        double bestScore = Double.MAX_VALUE;
        
        // Richieste interne
        for (Persona p : personeDentro) {
            int floor = p.getPianoDestinazione();
            double score = Math.abs(floor - pianoCorrente);
            if (score < bestScore) {
                bestScore = score;
                bestFloor = floor;
            }
        }
        // Richieste esterne (piani in coda)
        for (Piano piano : piani) {
            if (!piano.getCodaPersone().isEmpty()) {
                int floor = piano.getNumeroPiano();
                double score = Math.abs(floor - pianoCorrente) - alpha * piano.getTempoAttesa();
                if (score < bestScore) {
                    bestScore = score;
                    bestFloor = floor;
                }
            }
        }
        
        if (bestFloor > pianoCorrente) {
            salita();
        } else if (bestFloor < pianoCorrente) {
            discesa();
        } else {
            System.out.println(YELLOW + "[ASC] Rimani al piano " + pianoCorrente + " per servire la richiesta" + RESET);
        }
    }

    @Override
    public String toString() {
        return CYAN + "[ASC] Piano: " + pianoCorrente + " | Persone a bordo: " + personeDentro.size() +
                "/" + capienzaMassima + RESET;
    }
}
