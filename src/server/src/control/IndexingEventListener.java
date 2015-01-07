package control;

import java.util.EventListener;

public interface IndexingEventListener extends EventListener
{
	public void indexingEventOccurred(IndexingEvent evt);
}
