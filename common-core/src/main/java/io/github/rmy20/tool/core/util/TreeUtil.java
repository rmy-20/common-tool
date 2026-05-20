package io.github.rmy20.tool.core.util;

import io.github.rmy20.tool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 树化
 *
 * @author sheng
 */
public class TreeUtil {
    // region 树化

    /**
     * 树化
     *
     * @param nodeList        待树化列表
     * @param getIdFunc       获取id方法
     * @param getParentIdFunc 获取父节点id方法
     * @param setChildFunc    子节点列表设置方法
     * @param <Node>          待树化类型
     * @param <Id>            id类型
     */
    public static <Node, Id>
    List<Node> toTree(List<Node> nodeList, Function<Node, Id> getIdFunc, Function<Node, Id> getParentIdFunc,
                      BiConsumer<Node, List<Node>> setChildFunc) {
        Set<Id> idSet = new HashSet<>(16);
        Map<Optional<Id>, List<Node>> parentIdKeyMap = nodeList.stream().filter(Objects::nonNull)
                .collect(Collectors.groupingBy(node -> {
                    idSet.add(getIdFunc.apply(node));
                    return Optional.ofNullable(getParentIdFunc.apply(node));
                }, () -> new LinkedHashMap<>(16), Collectors.toList()));
        List<Node> resultList = new ArrayList<>();
        nodeList.forEach(node -> {
            setChildFunc.accept(node, parentIdKeyMap.getOrDefault(Optional.ofNullable(getIdFunc.apply(node)), Collections.emptyList()));
            Id parentId = getParentIdFunc.apply(node);
            if (Objects.isNull(parentId) || !idSet.contains(parentId)) {
                resultList.add(node);
            }
        });
        return resultList;
    }

    /**
     * 树化
     *
     * @param nodeList        待树化列表
     * @param convertFunc     将 Node 转化为 ResultNode方法
     * @param getIdFunc       获取id方法
     * @param getParentIdFunc 获取父节点id方法
     * @param setChildFunc    子节点列表设置方法
     * @param <Node>          待树化类型
     * @param <Id>            id类型
     */
    public static <Node, ResultNode, Id>
    List<ResultNode> toTree(List<Node> nodeList, Function<Node, ResultNode> convertFunc, Function<ResultNode, Id> getIdFunc,
                            Function<ResultNode, Id> getParentIdFunc, BiConsumer<ResultNode, List<ResultNode>> setChildFunc) {
        List<ResultNode> resultNodeList = nodeList.stream().filter(Objects::nonNull).map(convertFunc).collect(Collectors.toList());
        return toTree(resultNodeList, getIdFunc, getParentIdFunc, setChildFunc);
    }

    /**
     * 树化
     *
     * @param nodeList        待树化列表
     * @param getIdFunc       获取id方法
     * @param getParentIdFunc 获取父节点id方法
     * @param setChildFunc    子节点列表设置方法
     * @param filter          过滤，不符合的ResultNode剔除
     * @param <Node>          待树化类型
     * @param <Id>            id类型
     */
    public static <Node, Id>
    List<Node> toTree(List<Node> nodeList, Function<Node, Id> getIdFunc, Function<Node, Id> getParentIdFunc,
                      BiConsumer<Node, List<Node>> setChildFunc, Predicate<Node> filter) {
        Set<Id> idSet = new HashSet<>(16);
        Map<Optional<Id>, List<Node>> parentIdKeyMap = nodeList.stream().filter(Objects::nonNull)
                .collect(Collectors.groupingBy(node -> {
                    idSet.add(getIdFunc.apply(node));
                    return Optional.ofNullable(getParentIdFunc.apply(node));
                }, () -> new LinkedHashMap<>(16), Collectors.toList()));
        List<Node> resultList = new ArrayList<>();
        nodeList.stream().filter(filter).forEach(node -> {
            List<Node> childList = parentIdKeyMap.getOrDefault(Optional.ofNullable(getIdFunc.apply(node)), Collections.emptyList());
            childList.removeIf(child -> !filter.test(child));
            setChildFunc.accept(node, childList);
            Id parentId = getParentIdFunc.apply(node);
            if (Objects.isNull(parentId) || !idSet.contains(parentId)) {
                resultList.add(node);
            }
        });
        return resultList;
    }

    /**
     * 树化
     *
     * @param nodeList        待树化列表
     * @param convertFunc     将 Node 转化为 ResultNode方法
     * @param getIdFunc       获取id方法
     * @param getParentIdFunc 获取父节点id方法
     * @param setChildFunc    子节点列表设置方法
     * @param filter          过滤，不符合的ResultNode剔除
     * @param <Node>          待树化类型
     * @param <Id>            id类型
     */
    public static <Node, ResultNode, Id>
    List<ResultNode> toTree(List<Node> nodeList, Function<Node, ResultNode> convertFunc, Function<ResultNode, Id> getIdFunc,
                            Function<ResultNode, Id> getParentIdFunc, BiConsumer<ResultNode, List<ResultNode>> setChildFunc,
                            Predicate<ResultNode> filter) {
        List<ResultNode> resultNodeList = nodeList.stream().filter(Objects::nonNull).map(convertFunc).collect(Collectors.toList());
        return toTree(resultNodeList, getIdFunc, getParentIdFunc, setChildFunc, filter);
    }

    /**
     * 树化
     *
     * @param nodeList        待树化列表
     * @param getIdFunc       获取id方法
     * @param getParentIdFunc 获取父节点id方法
     * @param setChildFunc    子节点列表设置方法
     * @param rootPredicate   是否root节点
     */
    public static <Node, Id>
    List<Node> toRootTree(List<Node> nodeList, Function<Node, Id> getIdFunc, Function<Node, Id> getParentIdFunc,
                          BiConsumer<Node, List<Node>> setChildFunc, Predicate<Node> rootPredicate) {
        Map<Optional<Id>, List<Node>> parentIdKeyMap = nodeList.stream().filter(Objects::nonNull)
                .collect(Collectors.groupingBy(node -> Optional.ofNullable(getParentIdFunc.apply(node)),
                        () -> new LinkedHashMap<>(16), Collectors.toList()));
        List<Node> resultList = new ArrayList<>();
        for (Node node : nodeList) {
            if (rootPredicate.test(node)) {
                resultList.add(node);
            }
            setChildFunc.accept(node, parentIdKeyMap.getOrDefault(Optional.ofNullable(getIdFunc.apply(node)), Collections.emptyList()));
        }
        return resultList;
    }

    /**
     * 保留所有父级过滤，即存在叶子节点则保留所有父节点
     *
     * @param nodeTree     待过滤树
     * @param getChildFunc 获取子节点
     * @param filter       过滤方法
     * @param convertFunc  转换方法
     * @param setChildFunc 设置子节点
     * @param matchTodo    匹配则执行
     */
    public static <Node, ResultNode>
    List<ResultNode> filterTreeKeepParent(List<Node> nodeTree, Function<Node, List<Node>> getChildFunc, Predicate<Node> filter,
                                          Function<Node, ResultNode> convertFunc, BiConsumer<ResultNode, List<ResultNode>> setChildFunc,
                                          Consumer<ResultNode> matchTodo) {
        if (CollectionUtil.isEmpty(nodeTree)) {
            return Collections.emptyList();
        }
        List<ResultNode> result = new ArrayList<>(nodeTree.size());
        for (Node node : nodeTree) {
            List<ResultNode> childList = filterTreeKeepParent(getChildFunc.apply(node), getChildFunc, filter, convertFunc, setChildFunc, matchTodo);
            boolean test = filter.test(node);
            if (test || !childList.isEmpty()) {
                ResultNode resultNode = convertFunc.apply(node);
                setChildFunc.accept(resultNode, childList);
                result.add(resultNode);
                if (test && Objects.nonNull(matchTodo)) {
                    matchTodo.accept(resultNode);
                }
            }
        }
        return result;
    }

    /**
     * 保留所有父级过滤，即存在叶子节点则保留所有父节点
     *
     * @param nodeTree     待过滤树
     * @param getChildFunc 获取子节点
     * @param filter       过滤方法
     * @param convertFunc  转换方法
     * @param setChildFunc 设置子节点
     */
    public static <Node, ResultNode>
    List<ResultNode> filterTreeKeepParent(List<Node> nodeTree, Function<Node, List<Node>> getChildFunc, Predicate<Node> filter,
                                          Function<Node, ResultNode> convertFunc, BiConsumer<ResultNode, List<ResultNode>> setChildFunc) {
        return filterTreeKeepParent(nodeTree, getChildFunc, filter, convertFunc, setChildFunc, null);
    }

    // endregion
}
