package in.tosc.digitaloceanapp.Interfaces;


import java.util.List;

import in.tosc.doandroidlib.objects.Droplet;

/**
 * Created by rishabhkhanna on 19/05/17.
 */

public interface onDropletNameChange {
    void onSuccess(List<Droplet> modifiedData);

    void onError(String message);
}
