/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package browser;

import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.awt.event.MouseListener;
import java.io.*;
import java.net.*;


public class BrowserMain {

	static Browser browser;
	static Display display = new Display();
	static final Shell shell = new Shell(display);
	static String[] urls;
	static String[] titles;
	static int index;	
	static final Text location = new Text(shell, SWT.BORDER);
	static final Composite compTools = new Composite(shell, SWT.NONE);
	static final ToolBar navBar = new ToolBar(compTools, SWT.NONE);
	static Composite comp = new Composite(shell, SWT.NONE);
	static SashForm form = new SashForm(comp, SWT.HORIZONTAL);
	static final List list = new List(form, SWT.SINGLE);
	
	
	static MouseCatcher catcher = new MouseCatcher();
	
	public static void main(String argv[]) {
				
		
		shell.setText("SWT Browser - Documentation Viewer");
		shell.setLayout(new GridLayout());
		
		
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		
		compTools.setLayoutData(data);
		compTools.setLayout(new GridLayout(2, false));
		
		
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		location.setLayoutData(data);
		ToolBar tocBar = new ToolBar(compTools, SWT.NONE);
		ToolItem goItem = new ToolItem(tocBar, SWT.PUSH);
		goItem.setText("Go");
		ToolItem openItem = new ToolItem(tocBar, SWT.PUSH);
		openItem.setText("Browse");
		
		navBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_END));

		
		data = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(data);
		comp.setLayout(new FillLayout());
		
		
		form.setLayout(new FillLayout());
		
		try {
			browser = new Browser(form, SWT.NONE);
			browser.setMenu(new Menu(browser));
			browser.addMouseListener(catcher);
			browser.addMouseMoveListener(catcher);
			form.setWeights(new int[]{20,75});
		} catch (SWTError e) {
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR
					| SWT.OK);
			messageBox
					.setMessage("Closing application. The Browser could not be initialized.");
			messageBox.setText("Fatal error - application terminated");
			messageBox.open();
			System.exit(-1);
		}
		/*back.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				browser.back();
			}
		});
		forward.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				browser.forward();
			}
		});*/
		list.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				int index = list.getSelectionIndex();
				browser.setUrl(urls[index]);
			}
		});
		final LocationListener locationListener = new LocationListener() {
			public void changed(LocationEvent event) {
				Browser browser = (Browser) event.widget;
				//back.setEnabled(browser.isBackEnabled());
				//forward.setEnabled(browser.isForwardEnabled());
			}

			public void changing(LocationEvent event) {
			}
		};
		/*
		 * Build a table of contents. Open each HTML file found in the given
		 * folder to retrieve their title.
		 */
		final TitleListener tocTitleListener = new TitleListener() {
			public void changed(TitleEvent event) {
				titles[index] = event.title;
			}
		};
		final ProgressListener tocProgressListener = new ProgressListener() {
			public void changed(ProgressEvent event) {
			}

			public void completed(ProgressEvent event) {
				Browser browser = (Browser) event.widget;
				index++;
				boolean tocCompleted = index >= titles.length;
				if (tocCompleted) {
					browser.dispose();
					browser = new Browser(form, SWT.NONE);
					browser.setMenu(new Menu(browser));
					BrowserMain.browser = browser;
					form.layout(true);
					form.setWeights(new int[]{20,75});
					browser.addLocationListener(locationListener);
					browser.addMouseListener(catcher);
					browser.addMouseMoveListener(catcher);
					list.removeAll();
					for (int i = 0; i < titles.length; i++)
						list.add(titles[i]);
					list.select(0);
					browser.setUrl(urls[0]);
					shell.setText("SWT Browser - Documentation Viewer");
					return;
				}
				shell.setText("Building index " + index + "/" + urls.length);
				browser.setUrl(urls[index]);
			}
		};
		openItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				DirectoryDialog dialog = new DirectoryDialog(shell);
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
				urls = new String[files.length];
				titles = new String[files.length];
				index = 0;
				for (int i = 0; i < files.length; i++) {
					try {
						String url = files[i].toURL().toString();
						urls[i] = url;
					} catch (MalformedURLException ex) {
					}
				}
				shell.setText("Building index");
				browser.addTitleListener(tocTitleListener);
				browser.addProgressListener(tocProgressListener);
				/*browser.addMouseListener(catcher);
				browser.addMouseMoveListener(catcher);
				browser.setMenu(new Menu(browser));*/
				
				if (urls.length > 0)
					browser.setUrl(urls[0]);
			}
		});
		
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				ToolItem item = (ToolItem)event.widget;
				String string = item.getText();
				if (string.equals("Go")) browser.setUrl(location.getText());
		   }
		};
		goItem.addListener(SWT.Selection, listener);
		location.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				browser.setUrl(location.getText());
			}
		});

		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
				if (event.total == 0) return;                            
				
			}
			public void completed(ProgressEvent event) {
				location.setText(browser.getUrl());
			}
		});

		
		shell.open();
		browser.setUrl("http://moodle.inf.tu-dresden.de/");
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
}
