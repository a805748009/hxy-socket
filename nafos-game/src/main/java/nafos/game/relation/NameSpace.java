package nafos.game.relation;

import nafos.core.util.ObjectUtil;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年3月8日 下午6:05:07
 * 类说明
 */
public class NameSpace {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(NameSpace.class);

    final static ReadWriteLock spaceClientlock = new ReentrantReadWriteLock();

    final static ReadWriteLock spaceRoomlock = new ReentrantReadWriteLock();

    public static Set<Client> getClients(String nameSpace) {
        return IoCache.spaceClientMap.get(nameSpace);
    }


    public static void inviteClient(String nameSpace, Client client) {
        if (ObjectUtil.isNotNull(IoCache.spaceClientMap.get(nameSpace))) {
            spaceClientlock.writeLock().lock();
            IoCache.spaceClientMap.get(nameSpace).add(client);
            spaceClientlock.writeLock().unlock();
        } else {
            HashSet<Client> set = new HashSet<Client>();
            set.add(client);
            IoCache.spaceClientMap.put(nameSpace, set);
        }
    }


    public static void removeClient(String nameSpace, Client client) {
        if (ObjectUtil.isNotNull(nameSpace) && ObjectUtil.isNotNull(getClients(nameSpace))) {
            //防止在遍历的时候client被删除，导致报错
            spaceClientlock.writeLock().lock();
            IoCache.spaceClientMap.get(nameSpace).remove(client);
            spaceClientlock.writeLock().unlock();
        }
    }


    public static void sendMsg(String nameSpace, Object obj) {
        if (ObjectUtil.isNull(getClients(nameSpace))) {
            return;
        }
        spaceClientlock.readLock().lock();
        for (Client client : getClients(nameSpace)) {
            client.sendMsg(obj);
        }
        spaceClientlock.readLock().unlock();
    }

    public static Set<String> getRoomIds(String nameSpace) {
        return IoCache.spaceRoomMap.get(nameSpace);
    }

    /**
     * @param nameSpace
     * @param roomId
     */
    //创建房间
    public static void inviteRoom(String nameSpace, String roomId) {
        if (ObjectUtil.isNotNull(getRoomIds(nameSpace))) {
            spaceRoomlock.writeLock().lock();
            IoCache.spaceRoomMap.get(nameSpace).add(roomId);
            spaceRoomlock.writeLock().unlock();
        } else {
            HashSet<String> set = new HashSet<String>();
            set.add(roomId);
            IoCache.spaceRoomMap.put(nameSpace, set);
        }
    }


    public static void removeRoom(String nameSpace, String roomId) {
        if (ObjectUtil.isNotNull(getRoomIds(nameSpace))) {
            spaceRoomlock.writeLock().lock();
            IoCache.spaceRoomMap.get(nameSpace).remove(roomId);
            spaceRoomlock.writeLock().unlock();
        }
    }
}
