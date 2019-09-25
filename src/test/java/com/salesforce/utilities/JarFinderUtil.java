package com.salesforce.utilities;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class JarFinderUtil {
	private JarFile jarFile;

	public JarFinderUtil(String jarFile) throws IOException {
		this.jarFile = new JarFile(jarFile);

	}

	public Collection<String> getClasses(String pkg) {
		Enumeration<JarEntry> entries = jarFile.entries();
		boolean inclucdeChildPkg = false;
		if (pkg.endsWith(".*")) {
			inclucdeChildPkg = true;
			pkg = pkg.substring(0, pkg.length() - 2);
		}
		ArrayList<String> lst = new ArrayList<String>();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName().replaceAll("/", ".");
			if (name.endsWith(".class") && name.toLowerCase().startsWith(pkg.toLowerCase()) && (inclucdeChildPkg
					|| (StringUtil.countMatches(name, ".") == StringUtil.countMatches(pkg, ".") + 2))) {
				lst.add(name);
			}

		}
		return lst;
	}

	public Collection<String> getClassesBySimpleName(String name) {
		Enumeration<JarEntry> entries = jarFile.entries();
		ArrayList<String> lst = new ArrayList<String>();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry.getName().toLowerCase().endsWith(name.toLowerCase() + ".class")) {
				lst.add(entry.getName().replaceAll("/", "."));
			}
		}
		return lst;
	}

	public Collection<String> getAllClasses() {
		Enumeration<JarEntry> entries = jarFile.entries();
		ArrayList<String> lst = new ArrayList<String>();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry.getName().endsWith(".class")) {
				lst.add(entry.getName().replaceAll("/", "."));
			}
		}
		return lst;

	}

	public List<String> getAllPackages() {
		Enumeration<JarEntry> entries = jarFile.entries();
		ArrayList<String> lst = new ArrayList<String>();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry.isDirectory()) {
				String s = entry.getName();
				lst.add(s.substring(0, s.length()).replaceAll("/", "."));
			}
		}
		return lst;
	}

	public Collection<String> getAllMethods(String className) {
		return null;
	}

	public static void main(String[] args) {
		try {
			JarFinderUtil finderUtil = new JarFinderUtil("./dist/selenium-automation-framework.jar");
			Collection<String> lst = finderUtil.getClasses("com.qmetry.qaf.automation");
			for (Object element : lst) {
				System.out.println((String) element);

			}

			lst = finderUtil.getClassesBySimpleName("ConfigurationManager");
			for (Object element : lst) {
				System.out.println((String) element);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
