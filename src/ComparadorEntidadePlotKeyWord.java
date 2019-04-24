import java.util.Comparator;

public class ComparadorEntidadePlotKeyWord implements Comparator<EntidadePlotKeyWord> {
    
    @Override
    public int compare(EntidadePlotKeyWord o1, EntidadePlotKeyWord o2) {
        return o1.getPalavra().compareToIgnoreCase(o2.getPalavra());
    }
}