package at.crimsonbit.nodesystem.language;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.crimsonbit.nodesystem.nodebackend.util.Tuple;

public class LanguageSetup
{

	private String currentLanguage = "English";
	private LanguageFileReader reader = new LanguageFileReader();
	private static LanguageSetup instance;
	private Map<LangType, LanguageFile> langMap = new HashMap<>();

	public static LanguageSetup getInstance()
	{
		if (instance == null)
		{
			instance = new LanguageSetup();
		}
		return instance;
	}

	public static LanguageSetup getInstance(String lang)
	{
		if (instance == null)
		{
			instance = new LanguageSetup(lang);
		}
		return instance;
	}

	public LanguageSetup load(File... files)
	{
		LanguageFile f = null;
		for (File fi : files)
		{
			f = reader.readLanguageFile(fi);
			langMap.put(new LangType(f.getType(), f.getLang()), f);
		}
		return this;
	}

	public LanguageSetup load(Class cl, String... files)
	{
		LanguageFile f = null;
		for (String fi : files)
		{
			f = reader.readLanguageFile(new File(cl.getResource(fi).getFile()));
			langMap.put(new LangType(f.getType(), f.getLang()), f);
		}

		return this;
	}

	private LanguageSetup(String lang)
	{
		setLanguage(lang);
		readCoreLanguageFile();
	}

	private LanguageSetup()
	{
		readCoreLanguageFile();
	}

	public Set<String> getLanguages()
	{
		Set<String> langList = new HashSet<String>();
		for (LangType lt : langMap.keySet())
		{
			langList.add(lt.lang);
		}
		// System.out.println(langList);
		return langList;
	}

	private void readCoreLanguageFile()
	{
		LanguageFile f = null;
		try
		{
			f = reader.readLanguageFile(new File(getClass().getResource("en_core.strings").toURI()));
		} catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		langMap.put(new LangType(f.getType(), f.getLang()), f);
		try
		{
			f = reader.readLanguageFile(new File(getClass().getResource("de_core.strings").toURI()));
		} catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		langMap.put(new LangType(f.getType(), f.getLang()), f);

	}

	public String getLanguage()
	{
		return currentLanguage;
	}

	public void setLanguage(String currentLanguage)
	{
		this.currentLanguage = currentLanguage;
	}

	public String getString(String type, String token)
	{

		LanguageFile lang = langMap.get(new LangType(type, this.currentLanguage));

		if (lang != null)
		{
			String localized = lang.getString(token);
			if (localized != null)
				return localized;
		}
		return "STRING_NOT_FOUND";
	}

	public Tuple<String, String> getTokenFromString(String s)
	{
		Set<LangType> tmp = langMap.keySet();
		for (LangType t : tmp)
		{
			LanguageFile lang = langMap.get(t);
			for (String k : lang.getStrings().keySet())
			{
				if (lang.getStrings().get(k).equals(s))
				{
					return new Tuple<String, String>(k, lang.getType());
				}
			}

		}
		return new Tuple<String, String>("NULL", "NULL");
	}

	public void readLanguageFile(InputStream inStream)
	{
		LanguageFile f = reader.readLanguageFile(inStream);
		langMap.put(new LangType(f.getType(), f.getLang()), f);

	}

	private static class LangType
	{
		public final String type;
		public final String lang;

		public LangType(String type, String lang)
		{
			super();
			this.type = type;
			this.lang = lang;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((lang == null) ? 0 : lang.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			LangType other = (LangType) obj;
			if (lang == null)
			{
				if (other.lang != null)
					return false;
			} else if (!lang.equals(other.lang))
				return false;
			if (type == null)
			{
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}

	}

}
