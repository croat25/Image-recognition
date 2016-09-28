package finalllsssss;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
public class Main {

	private final static JFileChooser fileChooser = new JFileChooser();
	private final static JMenuItem EXIT = new JMenuItem("Exit"); // EXIT BUTTON
	private static File Fileo, output;
	private static BufferedImage bimg;
	private static JFrame frame;
	private static JPanel filterWest, featureWest, histogramWest, middle, recogEast;
	private static Dimension size;
	private static JScrollPane jsp, rlist;
	private static JLabel ximg, yimg, filterImage, canvasLabel;
	private static JTextField m1, m2, m3, m4, m5, m6, m7, m8, m9;
	private static JTextField fieldx,fieldy, recog_name;
	private static JTextArea featureRes, histRes, recogRes;
	private static JList<String> rememberedList;
	private static JButton highpassbutton, lowpassbutton, gaussbutton,vertbutton, horizbutton;
	private static Border border;
	private static ArrayList<Feature> features = new ArrayList<Feature>();
	private static int xh[],yh[];
	private static String[] templist;
	public static int X = 0;
	public static int Y = 1;
	private static int lX = -1;
	private static int lY = -1;
	static float featuresGrid[][];
	
	public static void setGUI(){
		frame = new JFrame(" Bruno Salapic and Edward Huang");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(size);
		frame.setResizable(true);
		frame.setLayout(new BorderLayout());
		guitopbar();
		guiCenter();
		guiFilterWest();
		guiFeatureWest();
		guiHistogramWest();
		guiRecogEast();
		
		frame.setVisible(true);
	}
	private static void guiCenter(){
		
		middle = new JPanel();
		middle.setLayout(new GridBagLayout());
		frame.add(middle,BorderLayout.CENTER);
	}
	
	private static void guitopbar(){

		//create menu bar
		JMenuBar menu = new JMenuBar();
		frame.add(menu,BorderLayout.NORTH);

		//file menu
		JMenu file = new JMenu("File");
		JMenuItem openfile = new JMenuItem("Open"); // OPEN BUTTON
		openfile.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int val = fileChooser.showOpenDialog(openfile);

				if (val == JFileChooser.APPROVE_OPTION){
					Fileo = fileChooser.getSelectedFile();

					readImage();
				}
			}

		});
		


		EXIT.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}


		});
		

		//edit menu
		JMenu edit = new JMenu("Edit");
		JMenuItem apply = new JMenuItem("Apply filter"); 
		apply.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.add(filterWest,BorderLayout.WEST);
				frame.remove(featureWest);
				frame.remove(histogramWest);
				featureWest.setVisible(false);
				histogramWest.setVisible(false);
				if (filterWest != null)
					filterWest.setVisible(!filterWest.isVisible());
			}

		});
		
		
		JMenuItem scaleimage = new JMenuItem("Scale");
		scaleimage.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				redisplay(scale(bimg,100,100,true));
			}
			
		});
		
		//Features menu
		JMenu fmenu = new JMenu("Features");
		//
		JMenuItem histogramtab = new JMenuItem("Histogram");
		
		histogramtab.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.add(histogramWest,BorderLayout.WEST);
				frame.remove(filterWest);
				frame.remove(featureWest);
				filterWest.setVisible(false);
				featureWest.setVisible(false);
				histogramWest.setVisible(!histogramWest.isVisible());
			}

		});
		
		JMenuItem cropimage = new JMenuItem("Auto Crop");
		
		cropimage.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				redisplay(Tools.autoCrop(bimg));
				
			}
		});
		
		JMenuItem thin = new JMenuItem("Thinning");
		
		thin.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(bimg != null){
					thinning(bimg);
					redisplay(null);
				}
			}

		});
		
		JMenuItem featgrid = new JMenuItem("Feature");
		
		featgrid.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.add(featureWest,BorderLayout.WEST);
				frame.remove(filterWest);
				frame.remove(histogramWest);
				filterWest.setVisible(false);
				histogramWest.setVisible(false);
				featureWest.setVisible(!featureWest.isVisible());
			}
		});
		
		JMenuItem recogn = new JMenuItem("Recognition");
		
		recogn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!recogEast.isVisible())
					drawListener();
				recogEast.setVisible(!recogEast.isVisible());
				//copied from click feature
				frame.add(featureWest,BorderLayout.WEST);
				frame.remove(filterWest);
				frame.remove(histogramWest);
				filterWest.setVisible(false);
				histogramWest.setVisible(false);
				featureWest.setVisible(true);
				frame.setSize(720,640);
			}
			
		});
		file.add(openfile);
		file.add(EXIT);
		menu.add(file);
		edit.add(apply);
		edit.add(scaleimage);
		menu.add(edit);
		menu.add(fmenu);
		fmenu.add(histogramtab);
		fmenu.add(cropimage);
		fmenu.add(thin);
		fmenu.add(featgrid);
		fmenu.add(recogn);
		//Number menu
		
		
		//Draw Menu
		
	}

	private static void guiRecogEast(){
		//setup
		recogEast = new JPanel();
		GridBagLayout g = new GridBagLayout(); 
		recogEast.setLayout(g);
		recogEast.setBackground(Color.GRAY);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		
		//erase
		JButton Erase = new JButton("Erase Image");
		Erase.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				drawListener();
			}

		});
		
		
		//List
		translateFeatures();
		rememberedList = new JList<String>(templist);
		rlist = new JScrollPane(rememberedList);
		rememberedList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                	try{
                	featuresGrid = features.get(rememberedList.getSelectedIndex()).getGrid();
					BufferedImage temp = createFeatureImage();
					temp = scale(temp,150,150,false);
					if(filterImage != null)
						featureWest.remove(filterImage);
					filterImage =new JLabel(new ImageIcon(temp));
					filterImage.setBorder(border);
					featureWest.add(filterImage);
					
					//redisplay(null);
					drawListener();
                	}catch(Exception e){}
                }
            }
        });
		recogEast.add(rlist);
		g.setConstraints(rlist,c);
		rlist.setBorder(border);
		
		
		//Text Name
		JLabel space = new JLabel(" ");
		g.setConstraints(space, c);
		recogEast.add(space);
		JLabel text_name = new JLabel("Name: ",JLabel.HORIZONTAL);
		recogEast.add(text_name);
		g.setConstraints(text_name, c);
		
		//Text Box for Name

		recog_name = new JTextField("",5); 
		g.setConstraints(recog_name, c);
		recog_name.setBorder(border);
		recogEast.add(recog_name);
		
		//Remember
		JButton buttonRemember = new JButton("Remember");
		buttonRemember.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				remember();
			}

		});
		recogEast.add(buttonRemember);
		g.setConstraints(buttonRemember, c);

		//Recognize
		JButton buttonRecognize = new JButton("Recognize");
		buttonRecognize.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				recognize();
			}

		});

		recogEast.add(buttonRecognize);
		g.setConstraints(buttonRecognize, c);
		//Delete
		JButton buttonDelete = new JButton("Delete");
		buttonDelete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteRecog();
			}

		});
		recogEast.add(Erase);
		g.setConstraints(Erase, c);
		recogEast.add(buttonDelete);
		g.setConstraints(buttonDelete, c);

		//Results
		recogRes = new JTextArea(6,6);
		JScrollPane textResults = new JScrollPane(recogRes);
		recogEast.add(textResults);
		g.setConstraints(textResults,c);
		textResults.setBorder(border);
		//setup
		recogEast.setVisible(false);
		frame.add(recogEast,BorderLayout.EAST);
	}
	
	private static void guiFeatureWest(){
		//FEATURE_EAST
		featureWest = new JPanel();
		GridBagLayout g = new GridBagLayout(); 
		featureWest.setLayout(g);
		featureWest.setBackground(Color.GRAY);

		//FEATURE ACCEPT GRID SIZE
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		JPanel feature = new JPanel();

		JLabel text_feature = new JLabel("Feature Grid size: ",JLabel.HORIZONTAL);
		JLabel yGrid = new JLabel("Y: ");
		JLabel xGrid = new JLabel("X: ");
		
		fieldy = new JTextField("6",5);
		fieldx = new JTextField("6",5); 
		

		fieldx.setBorder(border); 
		fieldy.setBorder(border);	 

		
		featureWest.setVisible(false);

		JButton calcFeat = new JButton("Calculate Features");
		calcFeat.setBorder(border);
		calcFeat.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(bimg != null){
					//make 1 or 0, store as feature vector
					if(bimg != null){
						redisplay(null);
						redisplay(Tools.autoCrop(bimg));
						String s = features(Integer.parseInt(fieldx.getText()),Integer.parseInt(fieldy.getText()),bimg);
						redisplay(null);
						featureRes.setText(s);
						BufferedImage temp = createFeatureImage();
						temp = scale(temp,100,100,false);
						if(filterImage != null)
							featureWest.remove(filterImage);
						
					}
					else
						featureRes.setText("cant generate.");
					redisplay(null);
				}
			}

		});
		
		
		JButton erase = new JButton("Erase Above");
		erase.setBorder(border);
		erase.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				featureRes.setText("");
			}

		});
		
		
		
		featureRes = new JTextArea(6,6);
		JScrollPane textFRes = new JScrollPane(featureRes);
		featureWest.add(textFRes);
		g.setConstraints(textFRes,c);
		textFRes.setBorder(border);
		
		featureWest.add(erase);
		g.setConstraints(erase,c);
		featureWest.add(text_feature);
		g.setConstraints(text_feature, c);
		feature.add(xGrid);
		feature.add(fieldx);
		feature.add(yGrid);
		feature.add(fieldy);
		featureWest.add(feature);
		g.setConstraints(feature, c);
		featureWest.add(calcFeat);
		g.setConstraints(calcFeat, c);
	
		
	}

	private static void guiFilterWest(){
		filterWest = new JPanel();
		GridBagLayout g = new GridBagLayout(); 
		filterWest.setLayout(g);

		//FILTER
		JPanel filter = new JPanel(new GridLayout(3,3));
		filter.setSize(50,50);

		border = BorderFactory.createLineBorder(Color.GRAY,2);
		m1 = new JTextField("-1",3);
		m2 = new JTextField("0");
		m3 = new JTextField("1");
		m4 = new JTextField("-2");
		m5 = new JTextField("0");
		m6 = new JTextField("2");
		m7 = new JTextField("-1");
		m8 = new JTextField("0");
		m9 = new JTextField("1");

		m1.setBorder(border);
		m2.setBorder(border);	 
		m3.setBorder(border);
		m4.setBorder(border); 
		m5.setBorder(border);	
		m6.setBorder(border);
		m7.setBorder(border); 
		m8.setBorder(border);	 
		m9.setBorder(border);

		filter.add(m1);
		filter.add(m2);
		filter.add(m3);
		filter.add(m4);
		filter.add(m5);
		filter.add(m6);
		filter.add(m7);
		filter.add(m8);
		filter.add(m9);
		filterWest.setVisible(false);

		JButton submit = new JButton("Apply");
		submit.setBorder(border);

		//GRID BAG on FILTER EAST
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;

		JLabel text_apply = new JLabel("Custom Filter: ",JLabel.HORIZONTAL);
		filterWest.setBackground(Color.GRAY);
		
		


		JLabel text_filters = new JLabel("Filters: ",JLabel.HORIZONTAL);
		

		highpassbutton = new JButton("High pass Filter");
		highpassbutton.setBorder(border);
		
		highpassbutton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				m1.setText("-1");
				m2.setText("-1");
				m3.setText("-1");
				m4.setText("-1");
				m5.setText("8");
				m6.setText("-1");
				m7.setText("-1");
				m8.setText("-1");
				m9.setText("-1");
			}

		});

		lowpassbutton = new JButton("Low pass Filter");
		lowpassbutton.setBorder(border);
		
		
		lowpassbutton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				m1.setText("1");
				m2.setText("1");
				m3.setText("1");
				m4.setText("1");
				m5.setText("1");
				m6.setText("1");
				m7.setText("1");
				m8.setText("1");
				m9.setText("1");
			}

		});
		horizbutton = new JButton("horizontal line filter");
		horizbutton.setBorder(border);
		
		
		horizbutton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				m1.setText("-1");
				m2.setText("-2");
				m3.setText("-1");
				m4.setText("0");
				m5.setText("0");
				m6.setText("0");
				m7.setText("1");
				m8.setText("2");
				m9.setText("1");
			}

		});
		vertbutton = new JButton("vertical line filter");
		vertbutton.setBorder(border);
		
		
		vertbutton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				m1.setText("-1");
				m2.setText("0");
				m3.setText("1");
				m4.setText("-2");
				m5.setText("0");
				m6.setText("2");
				m7.setText("-1");
				m8.setText("0");
				m9.setText("1");
			}

		});
		gaussbutton = new JButton("Gaussian Filter");
		gaussbutton.setBorder(border);
		
		gaussbutton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				m1.setText("1");
				m2.setText("2");
				m3.setText("1");
				m4.setText("2");
				m5.setText("4");
				m6.setText("2");
				m7.setText("1");
				m8.setText("2");
				m9.setText("1");
			}

		});
		
		JLabel text_space = new JLabel(" ",JLabel.HORIZONTAL);
		
		submit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				calculatePixels();
			}

		});
		
		filterWest.add(text_space);
		g.setConstraints(text_space, c);

		filterWest.add(text_apply);
		g.setConstraints(text_apply, c);
		filterWest.add(filter);
		g.setConstraints(filter, c);
		filterWest.add(text_filters);
		g.setConstraints(text_filters, c);
		filterWest.add(highpassbutton);
		g.setConstraints(highpassbutton, c);
		filterWest.add(lowpassbutton);
		g.setConstraints(lowpassbutton, c);
		filterWest.add(gaussbutton);
		g.setConstraints(gaussbutton, c);
		filterWest.add(horizbutton);
		g.setConstraints(horizbutton, c);
		filterWest.add(vertbutton);
		g.setConstraints(vertbutton, c);
		filterWest.add(submit);
		g.setConstraints(submit, c);
		frame.add(filterWest,BorderLayout.WEST);
	}
	private static void guiHistogramWest(){
		
		histogramWest = new JPanel();
		
		final GridBagLayout g = new GridBagLayout(); 
		histogramWest.setLayout(g);
		histogramWest.setBackground(Color.GRAY);

		//HISTOGRAM OPTIONS
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		histogramWest.setVisible(false);

		JButton histgen = new JButton("Generate Histogram");
		histgen.setBorder(border);
		histgen.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				histRes.setText("Histogram Generated.\n");
				if (bimg != null){
					BufferedImage tempx = displayHistogram(X);
					BufferedImage tempy = displayHistogram(Y);
					histogram(bimg);
					
					
					
					if(tempy.getHeight() > 200){
						tempy = scale(tempy,200,200,true);
					}
						
					else if(tempy.getWidth() > 200){
						tempy = scale(tempy,200,200,true);
					}
						
					if(tempx.getHeight() > 200){
						tempx = scale(tempx,200,200,true);
					}
						
					else if(tempx.getWidth() > 200)
					{
						tempx = scale(tempx,200,200,true);
					}
						
					if(ximg != null)
						
						histogramWest.remove(ximg);
					
					ximg =new JLabel(new ImageIcon(tempy));
					ximg.setBorder(border);
					histogramWest.add(ximg);
					
					if(yimg != null)
						
						histogramWest.remove(yimg);
					
					yimg =new JLabel(new ImageIcon(tempx));
					yimg.setBorder(border);
					histogramWest.add(yimg);
				}
				else
					histRes.setText("Cant generate.");
				redisplay(null);
			}

		});
		JButton histcrop= new JButton("Histogram with Cropping");
		histcrop.setBorder(border);
		histcrop.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				histRes.setText("Cropped Histogram.\n");
				if(bimg != null){
					
					redisplay(null);
					
					redisplay(Tools.autoCrop(bimg));
					histogram(bimg);
					BufferedImage tempx = displayHistogram(X);
					BufferedImage tempy = displayHistogram(Y);
					
					if(tempy.getHeight() > 200){
						tempy = scale(tempy,200,200,true);
					}
					else if(tempy.getWidth() > 200){
						tempy = scale(tempy,200,200,true);
					}
						
					if(tempx.getHeight() > 200){
						tempx = scale(tempx,200,200,true);
					}
					else if(tempx.getWidth() > 200){
						tempx = scale(tempx,200,200,true);
					}
						
					if(ximg != null)
						
						histogramWest.remove(ximg);
					
					ximg =new JLabel(new ImageIcon(tempy));
					ximg.setBorder(border);
					histogramWest.add(ximg);
					
					if(yimg != null)
						
						histogramWest.remove(yimg);
					
					yimg =new JLabel(new ImageIcon(tempx));
					yimg.setBorder(border);
					histogramWest.add(yimg);
				}
				else
					histRes.setText("Cant generate.");
				redisplay(null);
			}

		});
		histogramWest.add(histgen);
		g.setConstraints(histgen, c);
		histRes = new JTextArea(10,20);
		histogramWest.add(histRes);
		g.setConstraints(histRes,c);
		histRes.setBorder(border);
		histogramWest.add(histcrop);
		g.setConstraints(histcrop,c);
		
		JLabel space = new JLabel(" ");
		histogramWest.add(space);
		g.setConstraints(space,c);
		;
		
	
	}

	private static void redisplay(BufferedImage img){
		if(img != null)
			bimg = Tools.resize(img);
		middle.setVisible(false);
		if (jsp != null)
			frame.getContentPane().remove(jsp);
		canvasLabel = new JLabel(new ImageIcon(bimg));
		jsp = new JScrollPane(canvasLabel);
		frame.getContentPane().add(jsp);
		frame.setSize(size);
		frame.pack();
		
	}

	public static void histogram(BufferedImage img){
		Tools.setBlack(img);
		BufferedImage temp = Tools.chopBorder(img);
		yh = new int[temp.getHeight()];
		xh= new int[temp.getWidth()];
		
		for (int y = 0; y < temp.getHeight(); y++){
			for (int x = 0; x < temp.getWidth(); x++){
			
				Color c= new Color(temp.getRGB(x, y));
				if( c.getRed() == 0){
					//black
					xh[x]+=1;
					yh[y]+=1;
					
				}
			}
		}
		
		
		
	}


	public static BufferedImage thinning(BufferedImage img){
		
		Tools.setBlack(img);
		int pixelCount = 0;
		BufferedImage bimg = img;
		int[] grid = new int[9];
		
			
		
		for (int y = 2; y < bimg.getHeight()-2; y++){
			for (int x = 2; x < bimg.getWidth()-2; x++){
				grid = Tools.getBoundaryPixels(bimg,x,y);
				pixelCount = Tools.countPixels(grid); //# pixels surrounding pixel at x y
				if(pixelCount >=2 && pixelCount <= 6){
					if (Tools.count01s(grid) == 1 && grid[8] != 1){
						if(grid[1]*grid[3]*grid[5] == 0 && grid[3]*grid[5]*grid[7] == 0){ 
							grid[8]=1;bimg.setRGB(x, y, Color.white.getRGB()); 
						}
					}
				}
			}
		}

		for (int y =2; y < bimg.getHeight()-2; y++){
			 for (int x = 2; x < bimg.getWidth()-2; x++){
				grid = Tools.getBoundaryPixels(bimg,x,y);
				pixelCount = Tools.countPixels(grid); //# pixels surrounding pixel at x y
				if(pixelCount >=2 && pixelCount <= 6){
					if (Tools.count01s(grid) == 1 && grid[8]!=1){
						if(grid[1]*grid[3]*grid[7] == 0 && grid[1]*grid[5]*grid[7] == 0){ 
							grid[8]=1; bimg.setRGB(x, y, Color.white.getRGB()); 
						}
					}
				}
			}
		}
		return bimg;
	}

	private static String features(int gridx, int gridy, BufferedImage bimg){
		Tools.setBlack(bimg);
		BufferedImage temp = Tools.chopBorder(bimg);
		String res="";
		
		
		int yd = temp.getHeight();
		int xd = temp.getWidth();
		if(gridy > 0)
			yd = yd/gridy;
		if(gridx > 0)
			xd = xd/gridx; 
		 

		float [][] percGrid = new float[gridy][gridx];
		for (int j =0; j < gridx; j++){
			for (int i = 0; i < gridy; i++){
			
				percGrid[i][j] = Tools.percentBlack(xd*j, yd*i, xd, yd, temp);
			}
		}
		for(int j = 0;j<gridx;j++){
			for(int i = 0;i<gridy;i++){
			
				res+=String.format("%.2f ", percGrid[i][j]*100);	
			}
			res+="\n";
		}
		
		featuresGrid = percGrid;
		return res;
	}
	
	private static ArrayList<Featurec> compareFeatures(){
		float sum = 0;
		ArrayList<Featurec> results = new ArrayList<Featurec>();
		
		for (Feature f: features){
			sum = 0;
			for (int j = 0; j < featuresGrid[0].length; j++){
				for(int i = 0; i < featuresGrid.length; i++){
				
					if (featuresGrid[i][j] == f.grid[i][j]){
						sum++;
					}
				}
			}
			sum= sum/(featuresGrid.length*featuresGrid[0].length)*100;
			Featurec temp = new Featurec(f.name,sum);
			results.add(temp);
		}
		return results;
	}

	public static BufferedImage createFeatureImage(){
		BufferedImage result = new BufferedImage(featuresGrid[0].length,featuresGrid.length,BufferedImage.TYPE_INT_RGB);
		
		for(int j = 0; j < featuresGrid[0].length; j++){
			for (int i = 0; i < featuresGrid.length; i++){
			
				if(featuresGrid[i][j] > 0){
					featuresGrid[i][j] = 0;
					result.setRGB(j, i, Color.black.getRGB());
				}else{
					featuresGrid[i][j] = 1;
					result.setRGB(j, i, Color.white.getRGB());
				}
				
			}
		}
		return result;
	}
	
	private static void drawListener(){
		if(canvasLabel != null)
			frame.remove(canvasLabel);
		
		//frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
		
		bimg = new BufferedImage(400,400,BufferedImage.TYPE_INT_RGB);
		
		canvasLabel = new JLabel(new ImageIcon(bimg));
		for(int j = 1; j< bimg.getHeight()-1; j++){
			for(int i = 1; i< bimg.getWidth()-1; i++){
			
				bimg.setRGB(i, j, Color.white.getRGB());
			}
		}
		if(middle != null)
			frame.remove(middle);
		if(jsp != null)
			frame.remove(jsp);
		

		jsp = new JScrollPane(canvasLabel);
		frame.getContentPane().add(jsp);
		frame.pack();
		setDraw();
	}

	private static void remember(){
		if(!recog_name.getText().trim().isEmpty()){
		if(bimg != null){
			redisplay(null);
			redisplay(Tools.autoCrop(bimg));
			String s = features(6,6,bimg);
			
			fieldx.setText("6");
			fieldy.setText("6");
			redisplay(null);
			featureRes.setText(s);
			
			BufferedImage temp = createFeatureImage();
			temp = scale(temp,75,75,false);
			if(filterImage != null)
				featureWest.remove(filterImage);
			
			recordGrid(recog_name.getText().trim());
			try {
				writeFeature();
				recogRes.setText("Feature data for:\n'"+recog_name.getText().trim()+"' was saved.");
				translateFeatures();
				updateRememberedList();
			} catch (IOException e) {
				recogRes.setText("Could not save\ninto output file.");
			}
		}
		else
			featureRes.setText("Could not generate.");
		}else
			recogRes.setText("Enter a name .");
		

		
	}
	
	private static void recognize(){
		if(bimg != null){
			redisplay(null);
			redisplay(Tools.autoCrop(bimg));
			String s = features(6,6,bimg);
			fieldx.setText("6");
			fieldy.setText("6");
			redisplay(null);
			featureRes.setText(s);
			BufferedImage temp = createFeatureImage();
			temp = scale(temp,75,75,false);
			if(filterImage != null)
				featureWest.remove(filterImage);
			
			ArrayList<Featurec> results = compareFeatures();
			results = organizeFeatComps(results);
			displayFeatComps(results);
			//default title and icon
			JOptionPane.showMessageDialog(frame,
			    "The best match was '"+results.get(0).name+"'");
		}
		else
			featureRes.setText("Could not generate.");
	}
	private static void deleteRecog(){
		if(!rememberedList.isSelectionEmpty()){
			features.remove(rememberedList.getSelectedIndex());
			translateFeatures();
			updateRememberedList();
			try {
				writeFeature();
			} catch (IOException e) {
			}
		}
	}

	private static ArrayList<Featurec> organizeFeatComps(ArrayList<Featurec> in){
		ArrayList<Featurec> res = new ArrayList<Featurec>();
		
		Featurec high = null;
		int highn = 0;
		while(!in.isEmpty()){
			for (int i =0;i < in.size(); i++){
				Featurec f = in.get(i);
				if(high == null || f.percent > high.percent){
					high = f;
					highn = i;
				}
			}
			
			res.add(high);
			in.remove(highn);
			high = null;
		}
		
		return res;
	}
	
	

	private static void displayFeatComps(ArrayList<Featurec> in){
		String s = "";
		for (Featurec f: in){
			s+= f.name+" : "+f.percent+"\n";
		}
		recogRes.setText(s);
	}

	private static boolean recordGrid(String s){
		if (!s.isEmpty()){
			features= new ArrayList<Feature>(); //erase features
			

			try{
				readFeature();//read
				writeFeature();//write
			}catch(Exception e){
				e.printStackTrace();
			}
			features.add(new Feature(s,featuresGrid));  //add in new feature
			return true;
		}
		
		return false;
	}
	
	private static void readFeature() throws NumberFormatException, IOException{
		output = new File("featuredata.txt"); //gets file
		try {
			output.createNewFile(); // if not exist create new
		} catch (IOException e) {}
		BufferedReader br = new BufferedReader(new FileReader(output));
		String line;
		if(features == null)
			features = new ArrayList<Feature>();
		while ((line = br.readLine()) != null) {
			String s1[]= line.split(":");
			
			String[] temp = s1[1].split(",");
			float[][] f = new float[s1.length-1][temp.length];
			for (int i = 1; i < s1.length; i++){
				temp = s1[i].split(",");
				for(int j = 0; j < temp.length; j++){
					f[i-1][j] = Float.parseFloat(temp[j]);
				}
			}
			features.add(new Feature(s1[0],f));
		}
		br.close();
		
	}
	
	private static void writeFeature() throws IOException{
		output = new File("featuredata.txt"); //gets file
		try {
			output.createNewFile(); // if not exist create new
		} catch (IOException e) {}
		FileWriter fw = new FileWriter(output);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("");
		for (Feature f : features){
			String str = f.name+":";
			for(int j = 0; j < f.grid.length; j++){
				for(int i = 0; i < f.grid[0].length; i++){
					str += f.grid[j][i];
					if (j == f.grid.length-1 && i == f.grid[0].length-1)
						str += "\n";
					else if(i == f.grid[0].length-1)
						str += ":";
					else
						str += ",";
				}
			}
			bw.append(str);
		}
		bw.close();
	}
	
	private static void translateFeatures(){
		templist = new String[features.size()];
		for(int i = 0;i < features.size(); i++){
			templist[i] = features.get(i).name;
		}
	}

	private static void updateRememberedList(){
		rememberedList.setListData(templist);
	}
	
	

	private static BufferedImage scale(BufferedImage img, int Xs, int Ys, boolean chopBorder){
		
		BufferedImage res = new BufferedImage(Xs,Ys,BufferedImage.TYPE_INT_RGB);
		if(chopBorder)
			img = Tools.chopBorder(img);
		float sy = (float)Ys/(float)img.getHeight();
		float sx = (float)Xs/(float)img.getWidth();
		

	    if(img != null) {
	        Graphics2D g = res.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(sx, sy);
	        g.drawRenderedImage(img, at);
	    }

		return res;
	}
	
	private static BufferedImage displayHistogram(int mode){
		int height = yh.length;
		int width = xh.length;
		
		BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		
		if(mode == X)
			for(int x = 0; x < width; x++){
				for(int y = 0; y < height; y++){
					if(y < xh[x])
						img.setRGB(x, height-y-1, Color.black.getRGB());
					else
						img.setRGB(x, height-y-1,Color.white.getRGB());
				}
			}
			
		else if (mode == Y)
			for(int y = 0; y < height; y++){
				for(int x = 0; x < width; x++){
					if(x < yh[y])
						img.setRGB(x, y, Color.black.getRGB());
					else
						img.setRGB(x,y,Color.white.getRGB());
				}
			}
		return img;
	}

	private static void setDraw(){
		canvasLabel.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				try{
					if(bimg != null){
						//bimg.setRGB(e.getX(), e.getY(), Color.black.getRGB());
						Graphics2D g = bimg.createGraphics();
						g.setColor(Color.black);
						if(lX < 0 && lY <0){
							g.drawLine(e.getX(), e.getY()+1, e.getX(), e.getY()+1);
							g.drawLine(e.getX(), e.getY(), e.getX(), e.getY());
							
						}
						else{
							g.drawLine(lX,lY+1,e.getX(),e.getY()+1);
							g.drawLine(lX,lY,e.getX(),e.getY());
							
						}
						lY = e.getY();
						lX = e.getX();
						
						g.dispose();
						canvasLabel.repaint();
					}
				}
				catch(Exception ex){
					System.out.println("Cant draw");
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				
			}
			
		});
		
		canvasLabel.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				lY = -1;
				lX = -1;
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
			
		});
	}
	
	public static void readImage(){
		try {
			bimg = ImageIO.read(Fileo);
			bimg = Tools.resize(bimg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(bimg == null)
			System.out.println("Could not open file.");
		else{
			middle.setVisible(false);
			if(canvasLabel != null)
				frame.getContentPane().remove(canvasLabel);
			if (jsp != null)
				frame.getContentPane().remove(jsp);
			
			jsp = new JScrollPane(new JLabel(new ImageIcon(bimg)));
			frame.getContentPane().add(jsp);
			frame.pack();
		}
	}
	public static void calculatePixels(){
		//int[][] img = new int[bimg.getWidth()][bimg.getHeight()]; 
		try{

			int[] filter = {Integer.parseInt(m1.getText().trim()),
					Integer.parseInt(m2.getText().trim()),
					Integer.parseInt(m3.getText().trim()),
					Integer.parseInt(m4.getText().trim()),
					Integer.parseInt(m5.getText().trim()),
					Integer.parseInt(m6.getText().trim()),
					Integer.parseInt(m7.getText().trim()),
					Integer.parseInt(m8.getText().trim()),
					Integer.parseInt(m9.getText().trim()),};
			int sum = 0;
			for (int i = 0; i < filter.length; i++){
				sum+= filter[i];
			}
			if (sum == 0)
				sum = 1;
			
			int[][][] temp = new int[bimg.getWidth()][bimg.getHeight()][3];
			for (int y = 1; y < bimg.getHeight()-2; y++){
				for (int x = 1; x < bimg.getWidth()-2; x++){
				


					temp[x][y][0]=0;
					temp[x][y][1]=0;
					temp[x][y][2]=0;
					for (int k = 0; k < 3; k++){
						for (int i = 0; i < 3; i++){
						
							int red = bimg.getRGB(x-1+i, y-1+k)>>16&0xFF;
								red=red*filter[(i*3)+k];
							int green = bimg.getRGB(x-1+i, y-1+k)>>8&0xFF;
								green=green*filter[(i*3)+k];
							int blue = bimg.getRGB(x-1+i, y-1+k)>>0&0xFF;
								blue=blue*filter[(i*3)+k];
						
							temp[x][y][0]+=red;
							temp[x][y][1]+=green;
							temp[x][y][2]+=blue;
						}
					}
					temp[x][y][0]=temp[x][y][0] /sum;
					temp[x][y][1]=temp[x][y][1] /sum;
					temp[x][y][2]=temp[x][y][2] /sum;


				
				}
			}

			for (int j = 0; j < bimg.getHeight(); j++){
				for (int i = 0; i < bimg.getWidth(); i++){
				
					bimg.setRGB(i, j, new Color(
							Tools.within256(temp[i][j][0]), 
							Tools.within256(temp[i][j][1]),
							Tools.within256(temp[i][j][2])).getRGB());
				}
			}
			frame.repaint();

		}catch (Exception e){
			System.out.println("Error :"+e);
		}
	}
	public static void main (String [] args){
		size = new Dimension(800,800);
		setGUI();
		//read features into Array
		try {
			readFeature();
			//if successful translate into string array to display
			translateFeatures();
			updateRememberedList();
			templist = new String[features.size()];
			for(int i = 0;i < features.size(); i++){
				templist[i] = features.get(i).name;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

