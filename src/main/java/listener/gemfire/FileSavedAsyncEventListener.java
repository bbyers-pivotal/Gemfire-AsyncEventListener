package listener.gemfire;

import com.example.FileContents;
import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEvent;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class FileSavedAsyncEventListener implements AsyncEventListener, Declarable {

    private String path;

    public boolean processEvents(List<AsyncEvent> events) {
        for(AsyncEvent event: events) {
            FileContents fc = (FileContents) event.getDeserializedValue();
            try {
                FileOutputStream fos = new FileOutputStream(path + fc.getFilename());
                fos.write(fc.getContents());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void close() {

    }

    @Override
    public void init(Properties properties) {
        this.path = properties.getProperty("path");
    }
}
