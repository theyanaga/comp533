package comp533.summation;

import comp533.map.IntSumMapImpl;
import comp533.map.MapperFactory;
import comp533.mvc.Controller;
import comp533.mvc.ControllerImpl;
import comp533.mvc.Model;
import comp533.mvc.ModelImpl;
import comp533.mvc.ViewImpl;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;
import java.beans.PropertyChangeListener;

public class IntSummerMain extends AMapReduceTracer {

  public static void main(final String[] args) {
    MapperFactory.setMapper(new IntSumMapImpl());
    final Model model = new ModelImpl(MapperFactory.getMapper());

    final PropertyChangeListener view = new ViewImpl();
    model.addPropertyChangeListener(view);
    final Controller controller = new ControllerImpl(model);
    controller.run();
  }
}
