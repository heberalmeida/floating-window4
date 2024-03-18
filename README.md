# floating-window-cap

FloatingWindowCapacitor is a plugin that enables the creation of fixed floating windows on Android apps, making it easy to display important information persistently and conveniently.

## Install

```bash
npm install floating-window-cap
npx cap sync
```

## API

<docgen-index>

* [`showFloatingWindow(...)`](#showfloatingwindow)
* [`closeFloatingWindow()`](#closefloatingwindow)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### showFloatingWindow(...)

```typescript
showFloatingWindow(options: { url: string; }) => Promise<{ url: string; }>
```

| Param         | Type                          |
| ------------- | ----------------------------- |
| **`options`** | <code>{ url: string; }</code> |

**Returns:** <code>Promise&lt;{ url: string; }&gt;</code>

--------------------


### closeFloatingWindow()

```typescript
closeFloatingWindow() => Promise<null>
```

**Returns:** <code>Promise&lt;null&gt;</code>

--------------------

</docgen-api>
