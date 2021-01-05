package com.larryngo.shinycollector.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Pokemon implements Parcelable {
    private int id;
    private String name;
    private String nickname;
    private ArrayList<String> types;
    private String image_url;
    private byte[] image;

    public Pokemon() {
        id = 1;
        name = "Bulbasaur";
        nickname = "Bulbasaur";
        image = null;
        types = new ArrayList<>();
        types.add("grass");
        types.add("poison");
        image_url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png";
    }

    public Pokemon(int id, String name, ArrayList<String> types, String image_url) {
        this.id = id;
        this.name = name;
        this.nickname = name;
        this.types = types;
        this.image_url = image_url;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getDisplayName(int id, String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1); //capitalize first letter of word
        switch(id) {
            case 29:
                name = "Nidoran♀";
                break;
            case 32:
                name = "Nidoran♂";
                break;
            case 83:
                name = "Farfetch'd";
                break;
            case 122:
                name = "Mr. Mime";
                break;
            case 386:
                name = "Deoxys";
                break;
            case 413:
                name = "Wormadam";
                break;
            case 439:
                name = "Mime Jr.";
                break;
            case 487:
                name = "Giratina";
                break;
            case 492:
                name = "Shaymin";
                break;
            case 550:
                name = "Basculin";
                break;
            case 555:
                name = "Darmanitan";
                break;
            case 641:
                name = "Tornadus";
                break;
            case 642:
                name = "Thundurus";
                break;
            case 645:
                name = "Landorus";
                break;
            case 647:
                name = "Keldeo";
                break;
            case 648:
                name = "Meloetta";
                break;
            case 669:
                name = "Flabébé";
                break;
            case 678:
                name = "Meowstic";
                break;
            case 681:
                name = "Aegislash";
                break;
            case 710:
                name = "Pumpkaboo";
                break;
            case 711:
                name = "Gourgeist";
                break;
            case 741:
                name = "Oricorio";
                break;
            case 745:
                name = "Lycanroc";
                break;
            case 746:
                name = "Wishiwashi";
                break;
            case 772:
                name = "Type: Null";
                break;
            case 774:
                name = "Minior";
                break;
            case 778:
                name = "Mimikyu";
                break;
            case 785:
                name = "Tapu Koko";
                break;
            case 786:
                name = "Tapu Lele";
                break;
            case 787:
                name = "Tapu Bulu";
                break;
            case 788:
                name = "Tapu Fini";
                break;
        }

        return name;
    }

    protected Pokemon(Parcel in) {
        id = in.readInt();
        name = in.readString();
        nickname = in.readString();
        if (in.readByte() == 0x01) {
            types = new ArrayList<>();
            in.readList(types, String.class.getClassLoader());
        } else {
            types = null;
        }
        image_url = in.readString();
        image = in.createByteArray();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(nickname);
        if (types == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(types);
        }
        dest.writeString(image_url);
        dest.writeByteArray(image);
    }

    public static final Parcelable.Creator<Pokemon> CREATOR = new Parcelable.Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };
}