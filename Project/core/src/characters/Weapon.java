package characters;

public class Weapon {
	private WeaponType type;
	
	private int currentFireRate;
	private int clipCount;
	
	public Weapon(WeaponType type) {
		this.type = type;
	}
	
	public WeaponType getType() {
		return type;
	}
}
