package com.parzivail.swg.network;

import com.parzivail.util.math.lwjgl.Matrix4f;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public abstract class PMessage<REQ extends PMessage> implements Serializable, IMessage, IMessageHandler<REQ, IMessage>
{

	private static final HashMap<Class, Pair<Reader, Writer>> handlers = new HashMap<>();
	private static final HashMap<Class, Field[]> fieldCache = new HashMap<>();

	static
	{
		mapHandler(byte.class, PMessage::readByte, PMessage::writeByte);
		mapHandler(short.class, PMessage::readShort, PMessage::writeShort);
		mapHandler(int.class, PMessage::readInt, PMessage::writeInt);
		mapHandler(long.class, PMessage::readLong, PMessage::writeLong);
		mapHandler(float.class, PMessage::readFloat, PMessage::writeFloat);
		mapHandler(double.class, PMessage::readDouble, PMessage::writeDouble);
		mapHandler(boolean.class, PMessage::readBoolean, PMessage::writeBoolean);
		mapHandler(char.class, PMessage::readChar, PMessage::writeChar);
		mapHandler(String.class, PMessage::readString, PMessage::writeString);
		mapHandler(NBTTagCompound.class, PMessage::readNBT, PMessage::writeNBT);
		mapHandler(ItemStack.class, PMessage::readItemStack, PMessage::writeItemStack);
		mapHandler(BlockPos.class, PMessage::readBlockPos, PMessage::writeBlockPos);
		mapHandler(Matrix4f.class, PMessage::readMat4f, PMessage::writeMat4f);

		mapHandler(EntityPlayer.class, PMessage::readPlayer, PMessage::writePlayer);
		mapHandler(Entity.class, PMessage::readEntity, PMessage::writeEntity);
	}

	private static Field[] getClassFields(Class<?> clazz)
	{
		if (fieldCache.containsValue(clazz))
			return fieldCache.get(clazz);
		else
		{
			Field[] fields = clazz.getFields();
			Arrays.sort(fields, (Field f1, Field f2) -> {
				return f1.getName().compareTo(f2.getName());
			});
			fieldCache.put(clazz, fields);
			return fields;
		}
	}

	private static Pair<Reader, Writer> getHandler(Class<?> clazz)
	{
		Pair<Reader, Writer> pair = handlers.get(clazz);
		if (pair == null)
			throw new RuntimeException("No R/W handler for  " + clazz);
		return pair;
	}

	private static boolean acceptField(Field f, Class<?> type)
	{
		int mods = f.getModifiers();
		if (Modifier.isFinal(mods) || Modifier.isStatic(mods) || Modifier.isTransient(mods))
			return false;

		return handlers.containsKey(type);
	}

	public static <T extends Object> void mapHandler(Class<T> type, Reader<T> reader, Writer<T> writer)
	{
		handlers.put(type, Pair.of(reader, writer));
	}

	private static byte readByte(ByteBuf buf)
	{
		return buf.readByte();
	}

	private static void writeByte(byte b, ByteBuf buf)
	{
		buf.writeByte(b);
	}

	private static Matrix4f readMat4f(ByteBuf buf)
	{
		Matrix4f mat = new Matrix4f();
		mat.m00 = buf.readFloat();
		mat.m01 = buf.readFloat();
		mat.m02 = buf.readFloat();
		mat.m03 = buf.readFloat();
		mat.m10 = buf.readFloat();
		mat.m11 = buf.readFloat();
		mat.m12 = buf.readFloat();
		mat.m13 = buf.readFloat();
		mat.m20 = buf.readFloat();
		mat.m21 = buf.readFloat();
		mat.m22 = buf.readFloat();
		mat.m23 = buf.readFloat();
		mat.m30 = buf.readFloat();
		mat.m31 = buf.readFloat();
		mat.m32 = buf.readFloat();
		mat.m33 = buf.readFloat();
		return mat;
	}

	private static void writeMat4f(Matrix4f mat, ByteBuf buf)
	{
		buf.writeFloat(mat.m00);
		buf.writeFloat(mat.m01);
		buf.writeFloat(mat.m02);
		buf.writeFloat(mat.m03);
		buf.writeFloat(mat.m10);
		buf.writeFloat(mat.m11);
		buf.writeFloat(mat.m12);
		buf.writeFloat(mat.m13);
		buf.writeFloat(mat.m20);
		buf.writeFloat(mat.m21);
		buf.writeFloat(mat.m22);
		buf.writeFloat(mat.m23);
		buf.writeFloat(mat.m30);
		buf.writeFloat(mat.m31);
		buf.writeFloat(mat.m32);
		buf.writeFloat(mat.m33);
	}

	private static EntityPlayer readPlayer(ByteBuf buf)
	{
		long lsb = readLong(buf);
		long msb = readLong(buf);
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(new UUID(msb, lsb));
	}

	private static void writePlayer(EntityPlayer b, ByteBuf buf)
	{
		writeLong(b.getUniqueID().getLeastSignificantBits(), buf);
		writeLong(b.getUniqueID().getMostSignificantBits(), buf);
	}

	private static Entity readEntity(ByteBuf buf)
	{
		long lsb = readLong(buf);
		long msb = readLong(buf);
		try
		{
			return FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(new UUID(msb, lsb));
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}

	private static void writeEntity(Entity b, ByteBuf buf)
	{
		writeLong(b.getUniqueID().getLeastSignificantBits(), buf);
		writeLong(b.getUniqueID().getMostSignificantBits(), buf);
	}

	private static short readShort(ByteBuf buf)
	{
		return buf.readShort();
	}

	private static void writeShort(short s, ByteBuf buf)
	{
		buf.writeShort(s);
	}

	private static int readInt(ByteBuf buf)
	{
		return buf.readInt();
	}

	private static void writeInt(int i, ByteBuf buf)
	{
		buf.writeInt(i);
	}

	private static long readLong(ByteBuf buf)
	{
		return buf.readLong();
	}

	private static void writeLong(long l, ByteBuf buf)
	{
		buf.writeLong(l);
	}

	private static float readFloat(ByteBuf buf)
	{
		return buf.readFloat();
	}

	private static void writeFloat(float f, ByteBuf buf)
	{
		buf.writeFloat(f);
	}

	private static double readDouble(ByteBuf buf)
	{
		return buf.readDouble();
	}

	private static void writeDouble(double d, ByteBuf buf)
	{
		buf.writeDouble(d);
	}

	private static boolean readBoolean(ByteBuf buf)
	{
		return buf.readBoolean();
	}

	private static void writeBoolean(boolean b, ByteBuf buf)
	{
		buf.writeBoolean(b);
	}

	private static char readChar(ByteBuf buf)
	{
		return buf.readChar();
	}

	private static void writeChar(char c, ByteBuf buf)
	{
		buf.writeChar(c);
	}

	private static String readString(ByteBuf buf)
	{
		return ByteBufUtils.readUTF8String(buf);
	}

	private static void writeString(String s, ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, s);
	}

	private static NBTTagCompound readNBT(ByteBuf buf)
	{
		return ByteBufUtils.readTag(buf);
	}

	private static void writeNBT(NBTTagCompound cmp, ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, cmp);
	}

	private static ItemStack readItemStack(ByteBuf buf)
	{
		return ByteBufUtils.readItemStack(buf);
	}

	private static void writeItemStack(ItemStack stack, ByteBuf buf)
	{
		ByteBufUtils.writeItemStack(buf, stack);
	}

	private static BlockPos readBlockPos(ByteBuf buf)
	{
		return BlockPos.fromLong(buf.readLong());
	}

	private static void writeBlockPos(BlockPos pos, ByteBuf buf)
	{
		buf.writeLong(pos.toLong());
	}

	// The thing you override!
	public IMessage handleMessage(MessageContext context)
	{
		return null;
	}

	@Override
	public final IMessage onMessage(REQ message, MessageContext context)
	{
		if (context.side == Side.CLIENT)
			Minecraft.getMinecraft().addScheduledTask(() -> message.handleMessage(context));
		else
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> message.handleMessage(context));
		return null;
	}

	@Override
	public final void fromBytes(ByteBuf buf)
	{
		try
		{
			Class<?> clazz = getClass();
			Field[] clFields = getClassFields(clazz);
			for (Field f : clFields)
			{
				Class<?> type = f.getType();
				if (acceptField(f, type))
					readField(f, type, buf);
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Error at reading packet " + this, e);
		}
	}

	@Override
	public final void toBytes(ByteBuf buf)
	{
		try
		{
			Class<?> clazz = getClass();
			Field[] clFields = getClassFields(clazz);
			for (Field f : clFields)
			{
				Class<?> type = f.getType();
				if (acceptField(f, type))
					writeField(f, type, buf);
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Error at writing packet " + this, e);
		}
	}

	private final void writeField(Field f, Class clazz, ByteBuf buf) throws IllegalArgumentException, IllegalAccessException
	{
		Pair<Reader, Writer> handler = getHandler(clazz);
		handler.getRight().write(f.get(this), buf);
	}

	private final void readField(Field f, Class clazz, ByteBuf buf) throws IllegalArgumentException, IllegalAccessException
	{
		Pair<Reader, Writer> handler = getHandler(clazz);
		f.set(this, handler.getLeft().read(buf));
	}

	// Functional interfaces
	public interface Writer<T extends Object>
	{
		void write(T t, ByteBuf buf);
	}

	public interface Reader<T extends Object>
	{
		T read(ByteBuf buf);
	}
}
