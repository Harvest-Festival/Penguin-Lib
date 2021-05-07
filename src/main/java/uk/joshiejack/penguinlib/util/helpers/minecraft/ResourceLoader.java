package uk.joshiejack.penguinlib.util.helpers.minecraft;

public class ResourceLoader {/*
    private static WeakReference<ResourceLoader> instance;
    private final Multimap<String, String> dataToModIDs = HashMultimap.create();

    public ResourceLoader() { //Filter out forge, minecraft, mcp, and fml in our search for penguin mosd
        ModList.get().getMods().stream().filter(m -> !ignore(m.getModId()))
                .forEach(mc -> {
                    InputStream inputstream = null;
                    try {
                        String modid = mc.getModId();
                        inputstream = ResourceLoader.class.getResourceAsStream("/assets/" + mc.getModId() + "/penguin.json");
                        if (inputstream != null) {
                            String json = IOUtils.toString(inputstream, Charset.defaultCharset());
                            PenguinMod mod = GsonHelper.get().fromJson(json, PenguinMod.class);
                            if (mod != null && mod.penguinlib != null && mod.penguinlib.size() > 0) {
                                PenguinLib.LOGGER.info("Discovered the PenguinMod: " + modid + "; " +
                                        "Loading the resource directories: " + mod);
                                mod.penguinlib.forEach(data -> dataToModIDs.get(data).add(modid));
                            }
                        }
                    } catch (Throwable ignored) {
                    } finally {
                        IOUtils.closeQuietly(inputstream);
                    }
                });
    }

    public static ResourceLoader get() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new ResourceLoader());
        }

        return instance.get();
    }

    public static void clear() {
        instance = null;
    }

    public static <I> List<I> loadJson(Class<I> type, String dirLocation) {
        return loadJson(type, dirLocation, Sets.newHashSet());
    }

    public static <I> List<I> loadJson(Class<I> type, String dirLocation, Set<String> ignore) {
        List<I> list = Lists.newArrayList();
        File directory = new File(PenguinLib.getCustomFolder(), dirLocation);
        if (!directory.exists()) directory.mkdir();
        FileUtils.listFiles(directory, new String[]{"json"}, true)
                .forEach(file -> list.add(GsonHelper.get().fromJson(getStringFromFile(file), type)));
        String subdir = String.format("custom/%s", dirLocation);
        get().getResourceListInDirectory(ModList.get().get(), subdir, "json", Sets.newHashSet(), ignore).forEach(resource -> {
            String json = getJSONResource(resource, subdir);
            list.add(GsonHelper.get().fromJson(json, type));
        });
        return list;
    }

    public static <I> List<I> loadJsonForMod(Class<I> type, String dirLocation, String modid) {
        List<I> list = Lists.newArrayList();
        String subdir = String.format("custom/%s", dirLocation);
        Map<String, ModContainer> map = new HashMap<>();
        map.put(modid, Loader.instance().getIndexedModList().get(modid));
        get().getResourceListInDirectory(map, subdir, "json", Sets.newHashSet(), Sets.newHashSet()).forEach(resource -> {
            String json = getJSONResource(resource, subdir);
            list.add(GsonHelper.get().fromJson(json, type));
        });
        return list;
    }

    public static class PenguinMod {
        public List<String> penguinlib;

        public String toString() {
            return String.join(", ", penguinlib);
        }
    }

    private boolean ignore(String modid) {
        return modid.equals("mcp") || modid.equals("forge") || modid.equals("minecraft") || modid.equalsIgnoreCase("fml");
    }

    private boolean include(String directory, String modid) {
        if (ignore(modid)) return false;
        else {
            return dataToModIDs.get(directory).contains(modid);
        }
    }

    private static String remove(String input, String directory, String extension) {
        return input.replace("\\", "/").replace(String.format("%s/", "assets"), "").replace(String.format("%s/", directory), "").replace(String.format(".%s", extension), "");
    }

    public Set<ResourceLocation> getResourceListInDirectory(String directory, String extension) {
        return getResourceListInDirectory(directory, extension, Sets.newHashSet());
    }

    public Set<ResourceLocation> getResourceListInDirectory(String directory, String extension, Set<String> ignore) {
        return getResourceListInDirectory(Loader.instance().getIndexedModList(), directory, extension, ignore, Sets.newHashSet());
    }

    public Set<ResourceLocation> getResourceListInDirectory(Map<String, ModContainer> map, String directory, String extension, Set<String> ignore, Set<String> ignoreMods) {
        Set<ResourceLocation> resources = Sets.newHashSet();
        map.entrySet().stream().filter(e -> include(directory, e.getKey()) && !ignoreMods.contains(e.getKey())).forEach(entry -> {
            String modid = entry.getKey();
            try {
                String search_path = String.format("assets/%s/%s/", modid, directory);
                if (PenguinLib.IS_DEOBF) {
                    URL url = ResourceLoader.class.getResource(String.format("/%s", search_path));
                    if (url != null) {
                        Path path = new File(url.toURI()).toPath();
                        Files.walk(path).filter(n -> n.toString().endsWith("." + extension) && ignore.stream().noneMatch(n.toString()::contains))
                                .forEach(s -> resources.add(new ResourceLocation(modid, remove(path.relativize(s).toString(), directory, extension))));
                    }
                } else {
                    Path s = new File(String.format("assets/%s/%s", modid, directory)).toPath();
                    new ZipFile(entry.getValue().getSource()).stream()
                            .filter(n -> n.getName().startsWith(search_path) && n.getName().endsWith("." + extension) && ignore.stream().noneMatch(n.getName()::contains))
                            .map(e -> Paths.get(e.getName()))
                            .forEach(path -> resources.add(new ResourceLocation(modid, remove(s.relativize(path).toString(), directory, extension))));
                }
            } catch (IOException | URISyntaxException ignored) {
            }
        });

        return resources;
    }

    @SuppressWarnings("ConstantConditions")
    private static String getResource(ResourceLocation id, String directory, String suffix) {
        String s = id.getNamespace();
        String s1 = id.getPath();
        InputStream inputstream = null;

        try {
            inputstream = ResourceLoader.class.getResourceAsStream("/assets/" + s + "/" + directory + "/" + s1 + "." + suffix);
            return IOUtils.toString(inputstream, Charset.defaultCharset());
        } catch (Throwable ignored) {
        } finally {
            IOUtils.closeQuietly(inputstream);
        }

        return "";
    }

    public static String getJavaScriptResource(ResourceLocation id, String directory) {
        return getResource(id, directory, "js");
    }

    public static String getJSONResource(ResourceLocation id, String directory) {
        return getResource(id, directory, "json");
    }

    public static String getDatabaseResource(ResourceLocation id, String directory) {
        return getResource(id, directory, "csv");
    }

    public static String getStringFromFile(File l) {
        try {
            return FileUtils.readFileToString(l, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    } */
}
