package com.parzivail.util.item;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parzivail.swg.Resources;
import com.parzivail.swg.item.data.BlasterDescriptor;
import com.parzivail.swg.item.data.LightsaberDescriptor;
import com.parzivail.swg.register.ItemRegister;
import com.parzivail.util.common.Lumberjack;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModularItems
{
	private static final Gson GSON = new GsonBuilder().create();

	public static ArrayList<BlasterDescriptor> getBlasterDescriptors()
	{
		//		Gson gson = new GsonBuilder().registerTypeAdapter(BlasterDescriptor.class, ModuleBlasterDeserializer.INSTANCE).create();
		ArrayList<BlasterDescriptor> descriptors = new ArrayList<>();

		Path[] resourceFiles = getResourceFolderFiles("assets/" + Resources.MODID + "/modules/blasters");

		for (Path f : resourceFiles)
		{
			try (InputStream inputStream = Files.newInputStream(f))
			{
				try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream))
				{
					descriptors.add(GSON.fromJson(inputStreamReader, BlasterDescriptor.class));
				}
			}
			catch (Exception e)
			{
				Lumberjack.err("Failed to load blaster module: " + f.getFileName());
				e.printStackTrace();
			}
		}

		return descriptors;
	}

	public static ArrayList<LightsaberDescriptor> getLightsaberDescriptors()
	{
		//		Gson gson = new GsonBuilder().registerTypeAdapter(LightsaberDescriptor.class, ModuleLightsaberDeserializer.INSTANCE).create();
		ArrayList<LightsaberDescriptor> descriptors = new ArrayList<>();

		Path[] resourceFiles = getResourceFolderFiles("assets/" + Resources.MODID + "/modules/lightsabers");

		for (Path f : resourceFiles)
		{
			try (InputStream inputStream = Files.newInputStream(f))
			{
				try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream))
				{
					descriptors.add(GSON.fromJson(inputStreamReader, LightsaberDescriptor.class));
				}
			}
			catch (Exception e)
			{
				Lumberjack.err("Failed to load lightsaber module: " + f.getFileName());
				e.printStackTrace();
			}
		}

		return descriptors;
	}

	private static Path[] getResourceFolderFiles(String folder)
	{
		ClassLoader loader = ItemRegister.class.getClassLoader();
		URL url = loader.getResource(folder);
		try
		{
			Path path = getPath(url.toURI());
			return Files.list(path).toArray(Path[]::new);
		}
		catch (URISyntaxException | IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static Path getPath(URI uri) throws IOException
	{
		try
		{
			return Paths.get(uri);
		}
		catch (FileSystemNotFoundException e)
		{
			Map<String, String> env = new HashMap<>();
			env.put("create", "true");
			FileSystem dummy = FileSystems.newFileSystem(uri, env);
			return Paths.get(uri);
		}
	}
}
