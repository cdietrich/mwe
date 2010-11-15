/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.mwe.releng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class CopyProjectSettings {

	private static final String PROJECTSET_PSF = "./team/projectset/extssh/projectset.psf";

	public static void main(String[] args) {
		if (args.length!=2) {
			throw new IllegalArgumentException("call this class with includePatterns and excludePatterns "+ Arrays.asList(args));
		}
		new CopyProjectSettings().copy(PROJECTSET_PSF, args[0].split(","), args[1].split(","));
	}
	
	
	
	public void copy(String teamProjectSetFile, String[] includePatterns, String[] excludePatterns) {
		// <project reference="1.0,:extssh:dev.eclipse.org:/cvsroot/modeling,org.eclipse.tmf/org.eclipse.xtext/develop,develop"/>
		// <project reference="1.0,:extssh:dev.eclipse.org:/cvsroot/tools,org.eclipse.orbit/javax.xml,javax.xml,v1_3_4"/>
		final String projectPattern = "^([^,]*,){3}([^,\\\"]+)";
		copy(teamProjectSetFile, projectPattern, includePatterns, excludePatterns);
	}
	
	public void copy(String teamProjectSetFile, String projectPattern, String[] includePatterns, String[] excludePatterns) {
		try {
			Collection<String> projects = findProjects(teamProjectSetFile,projectPattern,includePatterns, excludePatterns);
			File[] settings = findSettingsToCopy();
			for(String project: projects) {
				System.out.println("Copying settings to "+project);
				copySettingsTo(settings, project);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private  final String naturePattern = "<nature>([^<]*)</nature>";

	private  final String jdtNatureName = "org.eclipse.jdt.core.javanature";
	private  final String pdePluginNatureName = "org.eclipse.pde.PluginNature";
	
	private  void copySettingsTo(File[] settings, String project) throws IOException {
		Pattern nature = Pattern.compile(naturePattern);
		File projectFile = new File(".." + File.separatorChar + project + File.separator + ".project");
		boolean jdtNature = false;
		boolean pdeNature = false;
		if (projectFile.exists()) {
			BufferedReader fileReader = new BufferedReader(new FileReader(projectFile));
			String line = null;
			while ((line = fileReader.readLine()) != null) {
				Matcher matcher = nature.matcher(line);
				if (matcher.find()) {
					String foundNature = matcher.group(1);
					if (foundNature.equals(jdtNatureName))
						jdtNature = true;
					else if (foundNature.equals(pdePluginNatureName))
						pdeNature = true;
				}
			}
			fileReader.close();
			copySettingsTo(settings, projectFile.getParentFile(), jdtNature, pdeNature);
		} else {
			System.err.println("Cannot find project file for: " + project);
		}
	}

	private  void copySettingsTo(File[] settings, File parentFile, boolean jdtNature, boolean pdeNature) throws IOException {
		File settingsDir = new File(parentFile, ".settings");
		if (!settingsDir.exists())
			settingsDir.mkdir();
		for(File setting: settings) {
			if (setting.getName().contains(".pde.") && !pdeNature)
				continue;
			if (setting.getName().contains(".jdt.") && !pdeNature)
				continue;
			copySettingsTo(setting, settingsDir);
		}
	}

	private  void copySettingsTo(File setting, File targetDir) throws IOException {
		File targetFile = new File(targetDir, setting.getName());
		if (!targetFile.exists()) {
			if(!targetFile.createNewFile()) {
				System.err.println("Cannot create file: " + targetFile);
				return;
			}
		}
		FileOutputStream outputStream = new FileOutputStream(targetFile);
		FileInputStream inputStream = new FileInputStream(setting);
		FileChannel outputChannel = outputStream.getChannel();
		FileChannel inputChannel = inputStream.getChannel();
		outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		outputChannel.close();
		inputChannel.close();
	}

	private  File[] findSettingsToCopy() {
		File settingsDir = new File(".settings");
		return settingsDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return !name.toLowerCase().contains("cvs");
			}
		});
	}

	private  Collection<String> findProjects(String teamProjectSetFile, String projectPattern, String[] includePatterns, String[] excludePatterns) throws FileNotFoundException, IOException {
		Pattern pattern = Pattern.compile(projectPattern);
		Pattern[] includes = new Pattern[includePatterns.length];
		for (int i = 0; i < includes.length; i++) {
			includes[i] = Pattern.compile(includePatterns[i]);
		}
		Pattern[] excludes = new Pattern[excludePatterns.length];
		for (int i = 0; i < excludes.length; i++) {
			excludes[i] = Pattern.compile(excludePatterns[i]);
		}
		Set<String> projectNames = new HashSet<String>();
		BufferedReader fileReader = new BufferedReader(new FileReader(teamProjectSetFile));
		String line;
		while ((line = fileReader.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				boolean matches = false;
				String project = matcher.group(2);
				for (Pattern includePattern : includes) {
					if (includePattern.matcher(project).matches()) {
						matches = true;
						break;
					}
				}
				for (Pattern excludePattern : excludes) {
					if (excludePattern.matcher(project).matches()) {
						matches = false;
						break;
					}
				}
				if (matches) {
					projectNames.add(project);
				}
			}
		}
		fileReader.close();
		if (projectNames.isEmpty())
			System.err.println("No match !");
		return projectNames;
	}
}
