package com.andrei1058.spigot.sidebar;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@SuppressWarnings("unused")
public class NarniaProvider extends SidebarProvider {

    private static SidebarProvider instance;

    @Override
    public Sidebar createSidebar(SidebarLine title, Collection<SidebarLine> lines, Collection<PlaceholderProvider> placeholderProviders) {
        return new NarniaSidebar(title, lines, placeholderProviders);
    }

    @Override
    public SidebarObjective createObjective(@NotNull WrappedSidebar sidebar,String name, boolean health, SidebarLine title, int type) {
        return ((NarniaSidebar)sidebar).createObjective(name, health ? IScoreboardCriteria.f : IScoreboardCriteria.a, title, type);
    }

    @Override
    public ScoreLine createScoreLine(WrappedSidebar sidebar, SidebarLine line, int score, String color) {
        return ((NarniaSidebar)sidebar).createScore(line, score, color);
    }

    public void sendScore(@NotNull WrappedSidebar sidebar, String playerName, int score) {
        if (sidebar.getHealthObjective() == null) return;
        PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(
                ScoreboardServer.Action.a, ((ScoreboardObjective)sidebar.getHealthObjective()).b(), playerName, score
        );
        for (Player player : sidebar.getReceivers()) {
            PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().b;
            playerConnection.a(packetPlayOutScoreboardScore);
        }
    }

    @Override
    public VersionedTabGroup createPlayerTab(WrappedSidebar sidebar, String identifier, SidebarLine prefix, SidebarLine suffix, boolean disablePushing) {
        return new NarniaPlayerList(sidebar, identifier, prefix, suffix, disablePushing);
    }

    @Override
    public void sendHeaderFooter(Player player, String header, String footer) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(new ChatComponentText(header), new ChatComponentText(footer));
        ((CraftPlayer)player).getHandle().b.a(packet);
    }

    public static SidebarProvider getInstance() {
        return null == instance ? instance = new NarniaProvider() : instance;
    }
}
