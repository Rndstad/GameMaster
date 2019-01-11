package net.amoebaman.gamemasterv3.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.amoebaman.gamemasterv3.softdepend.Depend;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Utils {
	
	public static List<Player> sort(Collection<Player> set){
		if(set == null)
			return null;
		List<Player> original = Lists.newArrayList(set);
		Collections.sort(original, new PlayerComparator());
		return original;
	}
	
	public static List<Set<Player>> split(List<Player> set, int divisions){
		List<Set<Player>> toReturn = new ArrayList<Set<Player>>(divisions);
		if(divisions <= 1){
			toReturn.add(Sets.newHashSet(set));
			return toReturn;
		}
		for(int i = 0; i < divisions; i++)
			toReturn.add(new HashSet<Player>());
		divisions--;
		int cycle = 0;
		boolean rising = true;
		for(Player player : set){
			if(rising)
				toReturn.get(cycle).add(player);
			else
				toReturn.get(divisions - cycle).add(player);
			cycle++;
			if(cycle >= divisions){
				cycle = 0;
				rising = !rising;
			}
		}
		return toReturn;
	}
	
	public static List<Player> halfShuffle(List<Player> list){
		List<Player> toReturn = new ArrayList<Player>();
		for(int i = 0; i < list.size(); i += 2){
			double random = Math.random();
			if(random > 0.5)
				toReturn.add(list.get(i));
			if(list.size() > i + 1)
				toReturn.add(list.get(i + 1));
			if(random <= 0.5)
				toReturn.add(list.get(i));
		}
		if(toReturn.size() < list.size())
			toReturn.addAll(list);
		return toReturn;
	}
	
	public static class PlayerComparator implements Comparator<Player>{
		public int compare(Player p1, Player p2) {
			if(Depend.hasStatMaster()){
				double elo1 = Depend.getStats().getStat(p1, "elo skill");
				double elo2 = Depend.getStats().getStat(p2, "elo skill");
				if(elo1 < elo2)
					return -1;
				if(elo1 > elo2)
					return 1;
				return 0;
			}
			else
				return p1.getName().compareTo(p2.getName());
		}
	}
	
	public static Location getHoloHudLoc(Player player){
		Location loc = player.getLocation().clone();
		double xOffset = Math.sin(-loc.getYaw() * Math.PI/180.0);
		double zOffset = Math.cos(-loc.getYaw() * Math.PI/180.0);
		loc.setX(loc.getX() + xOffset * 5);
		loc.setZ(loc.getZ() + zOffset * 5);
		return loc;
	}

	public static Material getMaterialByID(int id) {
		for (Material mat : Material.values()) {
			if (mat.getId() == id) {
				return mat;
			}
		}
		return null;
	}
	
}
