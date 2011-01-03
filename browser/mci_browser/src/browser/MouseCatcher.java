package browser;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Menu;

import lx.interaction.dollar.Dollar;
import lx.interaction.dollar.DollarListener;



public class MouseCatcher implements DollarListener, MouseListener, MouseMoveListener {

	int x,y,state;
	
	Dollar dollar = new Dollar(Dollar.GESTURES_DEFAULT);
	String name = "";
	double score = 0;
	boolean ok = false;
	
	public MouseCatcher() {
		dollar.setListener(this);
		dollar.setActive(true);
	}
		
	public void update(MouseEvent e)
	{	
		x = e.x;
		y = e.y;
	}	

	@Override
	public void dollarDetected(Dollar dollar) {
		score = dollar.getScore();
		name = dollar.getName();
		
		ok = score > 0.80;
		
		if(ok) {
			if(name.equals("circle CCW")) {
				BrowserMain.browser.refresh();
			} else if(name.equals("leftSquareBracket")) {
				BrowserMain.browser.back();
				BrowserMain.location.setText(BrowserMain.browser.getUrl());
			} else if(name.equals("rightSquareBracket")) {
				BrowserMain.browser.forward();
				BrowserMain.location.setText(BrowserMain.browser.getUrl());
			} else if(name.equals("v")) {
				openDialog();
			} else {
				System.out.println("Gesture: "+name);
			}
		}
	}
	
	private void openDialog() {
		DirectoryDialog dialog = new DirectoryDialog(BrowserMain.shell);
		String folder = dialog.open();
		if (folder == null)
			return;
		File file = new File(folder);
		File[] files = file.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".html") || name.endsWith(".htm");
			}
		});
		if (files.length == 0)
			return;
		BrowserMain.urls = new String[files.length];
		BrowserMain.titles = new String[files.length];
		BrowserMain.index = 0;
		for (int i = 0; i < files.length; i++) {
			try {
				String url = files[i].toURL().toString();
				BrowserMain.urls[i] = url;
			} catch (MalformedURLException ex) {
			}
		}
		
		final TitleListener tocTitleListener = new TitleListener() {
			public void changed(TitleEvent event) {
				BrowserMain.titles[BrowserMain.index] = event.title;
			}
		};
		final LocationListener locationListener = new LocationListener() {
			public void changed(LocationEvent event) {
				//BrowserMain.back.setEnabled(false);
				//BrowserMain.forward.setEnabled(false);
			}

			public void changing(LocationEvent event) {
			}
		};
		final ProgressListener tocProgressListener = new ProgressListener() {
			public void changed(ProgressEvent event) {
			}

			public void completed(ProgressEvent event) {
				Browser browser = (Browser) event.widget;
				BrowserMain.index++;
				boolean tocCompleted = BrowserMain.index >= BrowserMain.titles.length;
				if (tocCompleted) {
					browser.dispose();
					browser = new Browser(BrowserMain.form, SWT.NONE);
					browser.setMenu(new Menu(browser));
					BrowserMain.browser = browser;
					BrowserMain.form.layout(true);
					BrowserMain.form.setWeights(new int[]{20,75});
					browser.addLocationListener(locationListener);
					browser.addMouseListener(BrowserMain.catcher);
					browser.addMouseMoveListener(BrowserMain.catcher);
					BrowserMain.list.removeAll();
					for (int i = 0; i < BrowserMain.titles.length; i++)
						BrowserMain.list.add(BrowserMain.titles[i]);
					BrowserMain.list.select(0);
					browser.setUrl(BrowserMain.urls[0]);
					BrowserMain.shell.setText("SWT Browser - Documentation Viewer");
					return;
				}
				BrowserMain.shell.setText("Building index " + BrowserMain.index + "/" + BrowserMain.urls.length);
				browser.setUrl(BrowserMain.urls[BrowserMain.index]);
			}
		};
		
		BrowserMain.shell.setText("Building index");
		BrowserMain.browser.addTitleListener(tocTitleListener);
		BrowserMain.browser.addProgressListener(tocProgressListener);
		/*browser.addMouseListener(catcher);
		browser.addMouseMoveListener(catcher);
		browser.setMenu(new Menu(browser));*/
		
		if (BrowserMain.urls.length > 0)
			BrowserMain.browser.setUrl(BrowserMain.urls[0]);
	}

	@Override
	public void mouseMove(MouseEvent e) {
		state = 2;
		dollar.pointerDragged(e.x, e.y);				
		update(e);
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) { }

	@Override
	public void mouseDown(MouseEvent e) {
		state = 1;
		dollar.pointerPressed(e.x, e.y);		
		update(e);
	}

	@Override
	public void mouseUp(MouseEvent e) {
		if(state == 0)
			return;
		state = 0;
		dollar.pointerReleased(e.x, e.y);
		update(e);
	}

	

}
