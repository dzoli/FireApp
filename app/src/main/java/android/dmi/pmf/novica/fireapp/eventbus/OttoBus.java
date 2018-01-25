package android.dmi.pmf.novica.fireapp.eventbus;

import com.squareup.otto.Bus;

import org.androidannotations.annotations.EBean;

/**
 * Created by Novica on 6/6/2017.
 */

@EBean(scope = EBean.Scope.Singleton)
public class OttoBus extends Bus  {
    //this is EventBus this according Observer design pattern

}
