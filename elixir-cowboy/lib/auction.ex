defmodule Auction do
  @moduledoc false

  use Application


  def start(_type, _args) do
    children = [
      Plug.Cowboy.child_spec(
        scheme: :http,
        plug: Router,
        options: [
          dispatch: dispatch(),
          port: 8080
        ]
      ),
      Registry.child_spec(
        keys: :duplicate,
        name: Registry.Auction
      )
    ]

    opts = [strategy: :one_for_one, name: Auction.Application]
    Supervisor.start_link(children, opts)
  end

  defp dispatch do
    [
      {
        :_,
        [
          {"/auction", BidHandler, []},
          {:_, Plug.Adapters.Cowboy.Handler, {Router, []}}
        ]
      }
    ]
  end
end
